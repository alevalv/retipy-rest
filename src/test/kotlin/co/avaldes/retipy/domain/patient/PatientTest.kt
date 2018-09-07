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
import co.avaldes.retipy.domain.evaluation.optical.OpticalEvaluation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class PatientTest
{
    private val opticalEvaluation1 =
        OpticalEvaluation(1, 1, Date(), Date(), "", "", "", "", 0, 0, 0, 0, 0, 0, emptyMap<String, String>().toMutableMap(), "", emptyList())
    private val opticalEvaluation2 =
        OpticalEvaluation(2, 3, Date(), Date(), "", "", "", "", 0, 0, 0, 0, 0, 0, emptyMap<String, String>().toMutableMap(), "", emptyList())

    private lateinit var testInstance : Patient

    @BeforeEach
    fun setUp()
    {
        testInstance = Patient(
            1,
            "111",
            "aname",
            Date(),
            Sex.Female,
            "origin",
            "p",
            Education.Bachelor,
            "race",
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList())
    }

    @Test
    fun getMedicalRecords()
    {
        Assertions.assertTrue(testInstance.getOpticalEvaluations().isEmpty())
        testInstance.addOpticalEvaluation(opticalEvaluation1)
        Assertions.assertFalse(testInstance.getOpticalEvaluations().isEmpty())
        Assertions.assertEquals(opticalEvaluation1, testInstance.getOpticalEvaluations()[0])
    }

    @Test
    fun addMedicalRecord()
    {
        testInstance.addOpticalEvaluation(opticalEvaluation1)
        Assertions.assertEquals(1, testInstance.opticalEvaluationCount())
        testInstance.addOpticalEvaluation(opticalEvaluation2)
        Assertions.assertEquals(2, testInstance.opticalEvaluationCount())
    }

    @Test
    fun getMedicalRecord()
    {
        testInstance.addOpticalEvaluation(opticalEvaluation1)
        Assertions.assertEquals(opticalEvaluation1, testInstance.getOpticalEvaluation(1))
        Assertions.assertNull(testInstance.getOpticalEvaluation(1111))
    }

    @Test
    fun getMedicalRecord_Order()
    {
        testInstance.addOpticalEvaluation(opticalEvaluation1)
        testInstance.addOpticalEvaluation(opticalEvaluation2)
        Assertions.assertEquals(
            opticalEvaluation1, testInstance.getOpticalEvaluations()[0], "patient order is incorrect")
        Assertions.assertEquals(
            opticalEvaluation2, testInstance.getOpticalEvaluations()[1], "patient order is incorrect")
        val medicalRecord3 = OpticalEvaluation(2, 2, Date(), Date(), "", "", "", "", 0, 0, 0, 0, 0, 0, emptyMap<String, String>().toMutableMap(), "", emptyList())
        testInstance.addOpticalEvaluation(medicalRecord3)
        Assertions.assertEquals(
            medicalRecord3, testInstance.getOpticalEvaluations()[1], "patient order is incorrect")
    }
}
