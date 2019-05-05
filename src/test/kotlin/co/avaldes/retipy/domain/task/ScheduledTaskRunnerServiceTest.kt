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

package co.avaldes.retipy.domain.task

import co.avaldes.retipy.domain.evaluation.automated.IRetipyEvaluationService
import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation
import co.avaldes.retipy.domain.evaluation.automated.RetipyTask
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ScheduledTaskRunnerServiceTest {
    private val retipyUri = "http://localhost:15345"

    @RelaxedMockK
    private lateinit var retipyEvaluationService: IRetipyEvaluationService

    private val server: MockWebServer = MockWebServer()

    private lateinit var scheduledTaskRunnerService: ScheduledTaskRunnerService

    @BeforeAll
    fun setUpClass() {
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
        server.start(15345)
    }

    @AfterAll
    fun tearDownClass() {
        server.shutdown()
    }

    @BeforeEach
    fun setUp() {
        val evaluations = RetipyTask.values().map {
            RetipyEvaluation(diagnosticId = 1L, name = it, status = RetipyEvaluationStatus.Pending) }
        every { retipyEvaluationService.getPendingEvaluations() } returns evaluations
        every { retipyEvaluationService.getEvaluationsByStatus(RetipyEvaluationStatus.Running) } returns
            listOf(RetipyEvaluation(diagnosticId = 2L))
        every { retipyEvaluationService.save(any()) } returns RetipyEvaluation(diagnosticId = 2L)
        scheduledTaskRunnerService = ScheduledTaskRunnerService(retipyEvaluationService, retipyUri)
    }

    @Test
    fun runPendingTasks() {
        scheduledTaskRunnerService.runPendingTasks()

        verify(exactly = 1) { retipyEvaluationService.getPendingEvaluations() }
        verify(exactly = 1) {
            retipyEvaluationService.getEvaluationsByStatus(RetipyEvaluationStatus.Running)
        }
        verify { retipyEvaluationService.save(any()) }
    }
}
