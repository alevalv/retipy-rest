/*
 * Copyright (C) 2018 - Alejandro Valdes
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

package co.avaldes.retipy.domain.evaluation.automated

import co.avaldes.retipy.domain.diagnostic.Diagnostic
import co.avaldes.retipy.domain.diagnostic.IDiagnosticService
import co.avaldes.retipy.persistence.evaluation.retinal.IRetipyEvaluationRepository
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationBean
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import co.avaldes.retipy.rest.common.IncorrectInputException
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Test class for [RetipyEvaluationService]
 */
internal class RetipyEvaluationServiceTest {
    private val diagnosticId = 91283L

    private val retipyEvaluationBean = RetipyEvaluationBean(
        192873L,
        diagnosticId,
        RetipyTask.Segmentation.name,
        "an image",
        "",
        RetipyEvaluationStatus.Complete)

    private lateinit var retipyEvaluationService: RetipyEvaluationService
    private val mockRetipyRepository: IRetipyEvaluationRepository = mockk(relaxed = true)
    private val mockDiagnosticService: IDiagnosticService = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        retipyEvaluationService = RetipyEvaluationService(
            mockRetipyRepository, mockDiagnosticService)
        every { mockRetipyRepository.findByDiagnosticId(diagnosticId) } returns
            listOf(retipyEvaluationBean)
        every { mockRetipyRepository.findByStatus(RetipyEvaluationStatus.Complete) } returns
            listOf(retipyEvaluationBean)
        every { mockRetipyRepository.save(any<RetipyEvaluationBean>()) } returns
            retipyEvaluationBean
        every { mockDiagnosticService.get(diagnosticId) } returns Diagnostic()
    }

    @AfterEach
    fun tearDown() {
        clearMocks(mockRetipyRepository)
        clearMocks(mockDiagnosticService)
    }

    @Test
    fun findByDiagnostic() {
        Assert.assertEquals(
            "evaluations does not match",
            listOf(RetipyEvaluation.fromPersistence(retipyEvaluationBean)),
            retipyEvaluationService.findByDiagnostic(diagnosticId))
    }

    @Test
    fun getEvaluationsByStatus() {
        Assert.assertEquals(
            "evaluations does not match",
            listOf(RetipyEvaluation.fromPersistence(retipyEvaluationBean)),
            retipyEvaluationService.getEvaluationsByStatus(RetipyEvaluationStatus.Complete))

        Assert.assertTrue(
            "evaluations should be empty",
            retipyEvaluationService
                .getEvaluationsByStatus(RetipyEvaluationStatus.Running).isEmpty())
    }

    @Test
    fun getPendingEvaluations() {
        Assert.assertTrue(
            "evaluations should be empty",
            retipyEvaluationService.getPendingEvaluations().isEmpty())
    }

    @Test
    fun fromDiagnostic_RetipyTaskNone() {
        Assertions.assertThrows(IncorrectInputException::class.java) { retipyEvaluationService.fromDiagnostic(diagnosticId, RetipyTask.None) }
    }

    @Test
    fun fromDiagnostic_noDiagnostic() {
        every { mockDiagnosticService.get(diagnosticId) } throws IncorrectInputException("")
        Assertions.assertThrows(IncorrectInputException::class.java) { retipyEvaluationService.fromDiagnostic(diagnosticId, RetipyTask.Segmentation) }
    }

    @Test
    fun fromDiagnostic_existingComplete() {
        val task = RetipyTask.Segmentation
        every { mockRetipyRepository.findByDiagnosticIdAndName(diagnosticId, task.name) } returns
            listOf(RetipyEvaluation.toPersistence(
                RetipyEvaluation(
                    diagnosticId = diagnosticId, status = RetipyEvaluationStatus.Complete)))
        Assertions.assertThrows(IncorrectInputException::class.java) { retipyEvaluationService.fromDiagnostic(diagnosticId, task) }
    }

    @Test
    fun fromDiagnostic_noSegmentation() {
        every { mockRetipyRepository.findByDiagnosticIdAndName(
            diagnosticId, RetipyTask.Segmentation.name) } returns listOf()
        Assertions.assertThrows(IncorrectInputException::class.java) { retipyEvaluationService.fromDiagnostic(diagnosticId, RetipyTask.TortuosityFractal) }
    }

    @Test
    fun fromDiagnostic() {
        retipyEvaluationService.fromDiagnostic(diagnosticId, RetipyTask.Segmentation)
    }

    @Test
    fun deleteByDiagnostic() {
        retipyEvaluationService.deleteByDiagnostic(diagnosticId)
        verify { mockRetipyRepository.deleteByDiagnosticId(diagnosticId) }
    }

    @Test
    fun findByDiagnosticIdAndTask() {
        every { mockRetipyRepository.findByDiagnosticIdAndName(
                diagnosticId, RetipyTask.Segmentation.name) } returns listOf(retipyEvaluationBean)
        Assert.assertEquals(
            "evaluation does not match",
            RetipyEvaluation.fromPersistence(retipyEvaluationBean),
            retipyEvaluationService.findByDiagnosticIdAndTask(diagnosticId, RetipyTask.Segmentation))
    }

    @Test
    fun findByDiagnosticIdAndTask_null() {
        every { mockRetipyRepository.findByDiagnosticIdAndName(
                diagnosticId, RetipyTask.Segmentation.name) } returns listOf()
        Assert.assertNull(retipyEvaluationService.findByDiagnosticIdAndTask(diagnosticId, RetipyTask.Segmentation))
    }
}
