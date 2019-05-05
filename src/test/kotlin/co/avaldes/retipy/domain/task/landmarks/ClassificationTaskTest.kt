/*
 * Copyright (C) 2019 - Alejandro Valdes
 *
 * This file is part of retipy.
 *
 * retipy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * retipy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with retipy.  If not, see <http://www.gnu.org/licenses/>.
 */

package co.avaldes.retipy.domain.task.landmarks

import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation
import co.avaldes.retipy.domain.evaluation.automated.RetipyTask
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

public class ClassificationTaskTest {
    private val serverURL = "http://localhost:12497"

    private val retipyEvaluation = RetipyEvaluation(
        123,
        456,
        RetipyTask.LandmarksClassification,
        "myImage",
        listOf())

    private val server: MockWebServer = MockWebServer()

    private lateinit var testInstance: ClassificationTask

    @BeforeAll
    fun setupAll() {
        server.start(12497)
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                val body = request!!.body.readString(Charset.defaultCharset())
                if (body.contains("myImage")) {
                    return MockResponse()
                        .setHeader("content-type", "application/json")
                        .setResponseCode(200)
                        .setBody("{ \"bifurcations\": [], \"crossings\": [] }")
                }
                return MockResponse().setResponseCode(400)
            }
        }
    }

    @AfterAll
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun execute() {
        testInstance = ClassificationTask(serverURL, retipyEvaluation)

        val evaluation = testInstance.execute()
        assertEquals(retipyEvaluation.id, evaluation.id, "evaluation id does not match")
        assertEquals(
            RetipyEvaluationStatus.Complete,
            evaluation.status,
            "evaluation should be marked as completed")
        assertTrue(evaluation.rois.isEmpty(), "no roi should be added")
    }

    @Test
    fun execute_requestError() {
        val evaluation = RetipyEvaluation(
            123,
            456,
            RetipyTask.LandmarksClassification,
            "",
            listOf())
        testInstance = ClassificationTask(serverURL, evaluation)

        val resultEvaluation = testInstance.execute()
        assertEquals(evaluation.id, resultEvaluation.id, "evaluation id does not match")
        assertEquals(
            RetipyEvaluationStatus.Error,
            resultEvaluation.status,
            "evaluation should be marked with error")
        assertTrue(evaluation.rois.isEmpty(), "no roi should be added")
    }
}
