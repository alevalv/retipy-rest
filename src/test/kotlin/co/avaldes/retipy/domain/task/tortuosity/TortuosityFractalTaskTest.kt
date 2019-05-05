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

package co.avaldes.retipy.domain.task.tortuosity

import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

internal class TortuosityFractalTaskTest {
    private val serverUrl = "http://localhost:28591"
    private val evaluation = RetipyEvaluation(diagnosticId = 0L, image = "myImage")

    private lateinit var tortuosityTask: TortuosityFractalTask

    private val server: MockWebServer = MockWebServer()

    @BeforeAll
    fun setUp() {
        server.start(28591)
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                val body = request!!.body.readString(Charset.defaultCharset())
                if (body.contains("myImage")) {
                    return MockResponse()
                        .setHeader("content-type", "application/json")
                        .setResponseCode(200)
                        .setBody("""{"uri": "someuri",
                            "data": [{
                                "x": [1, 2, 3, 4],
                                "y": [5, 6, 7, 8],
                                "notes": "Fractal",
                                "color": "yellow"
                            }]}
                            """)
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
        tortuosityTask = TortuosityFractalTask(serverUrl, evaluation)
        val evaluated = tortuosityTask.execute()
        assertEquals(RetipyEvaluationStatus.Complete, evaluated.status, "status does not match")
        assertEquals(1, evaluated.rois.size)
        assertEquals("white", evaluated.rois.first().color)
    }

    @Test
    fun execute_error() {
        val evaluation = RetipyEvaluation(diagnosticId = 0L)
        tortuosityTask = TortuosityFractalTask(serverUrl, evaluation)
        val evaluated = tortuosityTask.execute()
        assertEquals(RetipyEvaluationStatus.Error, evaluated.status, "status does not match")
    }
}
