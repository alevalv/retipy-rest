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

package co.avaldes.retipy.domain.record

import co.avaldes.retipy.common.nm.Education
import co.avaldes.retipy.common.nm.Sex
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class PatientTest
{
    private val medicalRecord1 = Record(1, 1, Date(), Date(), "", "", "", "", 0, 0, 0, 0, 0, 0, "", "", 0)
    private val medicalRecord2 = Record(2, 3, Date(), Date(), "", "", "", "", 0, 0, 0, 0, 0, 0, "", "", 0)

    private var testInstance : Patient = Patient(
        1,
        111,
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
        emptyList())

    @BeforeEach
    fun setUp()
    {
        testInstance = Patient(
            1,
            111,
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
            emptyList())
    }

    @Test
    fun getMedicalRecords()
    {
        Assertions.assertTrue(testInstance.getMedicalRecords().isEmpty())
        testInstance.setMedicalRecord(medicalRecord1)
        Assertions.assertFalse(testInstance.getMedicalRecords().isEmpty())
        Assertions.assertEquals(medicalRecord1, testInstance.getMedicalRecords()[0])
    }

    @Test
    fun addMedicalRecord()
    {
        testInstance.setMedicalRecord(medicalRecord1)
        Assertions.assertEquals(1, testInstance.recordCount())
        testInstance.setMedicalRecord(medicalRecord2)
        Assertions.assertEquals(2, testInstance.recordCount())
    }

    @Test
    fun getMedicalRecord()
    {
        testInstance.setMedicalRecord(medicalRecord1)
        Assertions.assertEquals(medicalRecord1, testInstance.getMedicalRecord(1))
        Assertions.assertNull(testInstance.getMedicalRecord(1111))
    }

    @Test
    fun getMedicalRecord_Order()
    {
        testInstance.setMedicalRecord(medicalRecord1)
        testInstance.setMedicalRecord(medicalRecord2)
        Assertions.assertEquals(
            medicalRecord1, testInstance.getMedicalRecords()[0], "record order is incorrect")
        Assertions.assertEquals(
            medicalRecord2, testInstance.getMedicalRecords()[1], "record order is incorrect")
        val medicalRecord3 = Record(2, 2, Date(), Date(), "", "", "", "", 0, 0, 0, 0, 0, 0, "", "", 0)
        testInstance.setMedicalRecord(medicalRecord3)
        Assertions.assertEquals(
            medicalRecord3, testInstance.getMedicalRecords()[1], "record order is incorrect")
    }

    @Test
    fun mappers()
    {
        val patientBean = Patient.toPersistence(testInstance)
        val patient = Patient.fromPersistence(patientBean)
        Assertions.assertTrue(testInstance == patient, "mapping failed")
    }

}
