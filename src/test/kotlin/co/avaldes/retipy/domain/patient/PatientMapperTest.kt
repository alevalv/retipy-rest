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
import co.avaldes.retipy.domain.common.Person
import co.avaldes.retipy.security.domain.user.IUserService
import co.avaldes.retipy.security.domain.user.User
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class PatientMapperTest
{
    private val doctorId = 12354L
    private val doctorIdentity = "1231231"
    private val doctorName = "Doctor D"
    private val person = Person(doctorId, doctorIdentity, doctorName)
    private lateinit var patient : Patient
    private lateinit var testInstance: PatientMapper

    private val mockUserService: IUserService = mockk(relaxed = true)

    @BeforeEach
    fun setUp()
    {
        clearMocks(mockUserService)

        patient = Patient(
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
            listOf(person))

        testInstance = PatientMapper(mockUserService)

        every { mockUserService.get(doctorId) } returns (
            User(
                doctorId,
                doctorIdentity,
                doctorName,
                "" ,
                "",
                mutableSetOf(),
                true,
                false,
                false))
    }

    @Test
    fun testMapping()
    {
        val bean = testInstance.toPersistence(patient)
        val domain = testInstance.fromPersistence(bean)

        Assert.assertEquals("Mapped patient does not match", patient, domain)

    }
}
