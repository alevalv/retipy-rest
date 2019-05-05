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

package co.avaldes.retipy.domain.task.vessels

import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation
import co.avaldes.retipy.domain.evaluation.automated.RetipyTask
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import java.nio.charset.Charset

internal class VesselsClassificationTaskTest {
    private val serverURL = "http://localhost:12597"

    private val retipyEvaluation = RetipyEvaluation(
        123,
        456,
        RetipyTask.VesselsClassification,
        "myImage",
        listOf())

    private val segmentedRetipyEvaluation = RetipyEvaluation(
        124,
        457,
        RetipyTask.VesselsClassification,
        "mySegmentedImage",
        listOf())

    private val server: MockWebServer = MockWebServer()

    private lateinit var testInstance: VesselsClassificationTask

    @BeforeAll
    fun setupAll() {
        server.start(12597)
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                val body = request!!.body.readString(Charset.defaultCharset())
                if (body.contains("myImage")) {
                    return MockResponse()
                        .setHeader("content-type", "application/json")
                        .setResponseCode(200)
                        .setBody("{\"classification\": \"newImage123\" }")
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
        testInstance = VesselsClassificationTask(
                serverURL, retipyEvaluation = retipyEvaluation, segmentedRetipyEvaluation = segmentedRetipyEvaluation)
        val classificationResult = testInstance.execute()
        assertEquals(
            RetipyEvaluationStatus.Complete,
            classificationResult.status,
            "status should be completed")
        assertEquals(
            "newImage123",
            classificationResult.image,
            "new image does not match")
    }

    @Test
    fun execute_requestError() {
        val evaluation = RetipyEvaluation(
            123,
            456,
            RetipyTask.VesselsClassification,
            "",
            listOf())
        testInstance = VesselsClassificationTask(
                serverURL, retipyEvaluation = evaluation, segmentedRetipyEvaluation = segmentedRetipyEvaluation)
        val resultEvaluation = testInstance.execute()
        assertEquals(evaluation.id, resultEvaluation.id, "evaluation id does not match")
        assertEquals(evaluation.image, resultEvaluation.image, "image should have not changed")
        assertEquals(
            RetipyEvaluationStatus.Error,
            resultEvaluation.status,
            "evaluation should be marked with error")
    }

    @Test
    fun execute_requestNullError() {
        testInstance = VesselsClassificationTask(
                serverURL, retipyEvaluation = retipyEvaluation, segmentedRetipyEvaluation = null)
        val resultEvaluation = testInstance.execute()
        assertEquals(retipyEvaluation.id, resultEvaluation.id, "evaluation id does not match")
        assertEquals(retipyEvaluation.image, resultEvaluation.image, "image should have not changed")
        assertEquals(
                RetipyEvaluationStatus.Error,
                resultEvaluation.status,
                "evaluation should be marked with error")
    }
}
