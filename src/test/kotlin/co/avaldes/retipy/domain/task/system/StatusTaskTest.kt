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

package co.avaldes.retipy.domain.task.system

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

internal class StatusTaskTest {
    private val serverUri = "http://localhost:42312"

    private val statusTask = StatusTask(serverUri)
    private val server: MockWebServer = MockWebServer()

    @BeforeAll
    fun setUp() {
        server.start(42312)
        server.dispatcher = object : Dispatcher() {
            var sent = true
            override fun dispatch(request: RecordedRequest?): MockResponse {
                if (sent) {
                    sent = false
                    return MockResponse().setResponseCode(200)
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
        assertTrue(statusTask.execute(), "should return true")
    }

    @Test
    fun execute_error() {
        server.shutdown()
        assertFalse(statusTask.execute(), "should return false")
    }
}
