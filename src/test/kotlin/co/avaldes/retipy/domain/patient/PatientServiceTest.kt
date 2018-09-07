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

package co.avaldes.retipy.domain.patient

import co.avaldes.retipy.common.nm.Education
import co.avaldes.retipy.common.nm.Sex
import co.avaldes.retipy.domain.diagnostic.IDiagnosticService
import co.avaldes.retipy.persistence.diagnostic.DiagnosticBean
import co.avaldes.retipy.persistence.diagnostic.DiagnosticStatus
import co.avaldes.retipy.persistence.evaluation.optical.OpticalEvaluationBean
import co.avaldes.retipy.persistence.patient.IPatientRepository
import co.avaldes.retipy.persistence.patient.PatientBean
import co.avaldes.retipy.security.domain.user.IUserService
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class PatientServiceTest
{
    val patientId = 1L
    val opticalEvaluationId = 2L
    val diagnosticId = 3L
    val image = "this is an image"

    private val opticalEvaluationBean = OpticalEvaluationBean(
        opticalEvaluationId, 1, Date(), Date(), "", "", "", "", 1, 1, 1, 1, 1, 1, "", "")

    private val patientBean = PatientBean(
        patientId,
        "12123123",
        "", Date(),
        Sex.Female, "",
        "",
        Education.Bachelor,
        "",

        "",
        "",
        "",
        listOf(opticalEvaluationBean),
        "")

    private val diagnosticBean = DiagnosticBean(diagnosticId, image, "", "{}", DiagnosticStatus.CREATED, Date(), Date())

    val opticalEvaluationBeanUpdated = opticalEvaluationBean.copy(diagnostics = listOf(diagnosticBean))

    val patientBeanUpdated = patientBean.copy(opticalEvaluations = listOf(opticalEvaluationBeanUpdated))

    private val mockPatientRepository: IPatientRepository = mockk(relaxed = true)
    private val mockDiagnosticService: IDiagnosticService = mockk(relaxed = true)
    private val mockUserService: IUserService = mockk(relaxed = true)
    private lateinit var testInstance: PatientService

    @BeforeEach
    fun setUp()
    {
        clearMocks(mockPatientRepository)
        clearMocks(mockDiagnosticService)
        clearMocks(mockUserService)
        testInstance = PatientService(
            mockPatientRepository, mockDiagnosticService, PatientMapper(mockUserService))
        every { mockPatientRepository.findById(patientId) } returns Optional.of(patientBean)
        every { mockPatientRepository.save(any<PatientBean>()) } returns patientBeanUpdated
    }

    @Test
    fun testSaveDiagnosticOnlyImage()
    {
        val diagnostic = testInstance.saveDiagnosticByImage(1 ,2, image)
        Assert.assertEquals(diagnostic.id, diagnosticId)
    }

    @Test
    fun test_getAllPatients()
    {
        every { mockPatientRepository.findAll() } returns listOf(patientBean)

        val patientList = testInstance.getAllPatients()
        Assert.assertEquals(1, patientList.size)
        Assert.assertEquals(patientId, patientList.first().id)
    }
}
