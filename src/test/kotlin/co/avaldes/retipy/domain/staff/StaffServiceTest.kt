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

package co.avaldes.retipy.domain.staff

import co.avaldes.retipy.domain.common.Person
import co.avaldes.retipy.persistence.staff.DoctorAssignedResidentsBean
import co.avaldes.retipy.persistence.staff.IDoctorAssignedResidentsRepository
import co.avaldes.retipy.security.domain.user.IUserService
import co.avaldes.retipy.security.domain.user.User
import co.avaldes.retipy.security.persistence.user.Role
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

/**
 * Test class for [StaffService].
 */
internal class StaffServiceTest {
    private val doctorId = 90173L
    private val resident1Id = 9123L
    private val resident1Name = "a1 name"
    private val resident1Identity = "AO192731231"
    private val resident2Id = 56232L
    private val resident2Name = "a2 name"
    private val resident2Identity = "AO889009809"
    private val doctorAssignedResidentsBean = DoctorAssignedResidentsBean(doctorId, listOf())
    private val doctor = User(doctorId, "uen", "o1ueln", "ouasnd", "aosid", mutableSetOf(Role.Doctor))
    private val resident1 = User(resident1Id, resident1Identity, resident1Name, "anjd", "1j9la")
    private val resident2 = User(resident2Id, resident2Identity, resident2Name, "anjd", "1j9la")

    private val mockIDoctorAssignedResidentsRepository: IDoctorAssignedResidentsRepository =
        mockk(relaxed = true)
    private val mockIUserService: IUserService = mockk(relaxed = true)

    private val slot = slot<DoctorAssignedResidentsBean>()

    private lateinit var staffService: StaffService
    @BeforeEach
    fun setUp() {
        clearMocks(
            mockIDoctorAssignedResidentsRepository,
            mockIUserService)
        every { mockIUserService.find(any()) } returns null
        every { mockIUserService.find(resident1Id) } returns resident1
        every { mockIUserService.find(resident2Id) } returns resident2
        every { mockIUserService.get(doctorId) } returns doctor
        every { mockIDoctorAssignedResidentsRepository.save(capture(slot)) } returns
            DoctorAssignedResidentsBean(doctorId, listOf())

        staffService = StaffService(
            mockIDoctorAssignedResidentsRepository,
            mockIUserService)

        every { mockIDoctorAssignedResidentsRepository.findById(doctorId) } returns
            Optional.of(doctorAssignedResidentsBean)
    }

    @Test
    fun getDoctorAssignedResidents_empty() {
        val residents = staffService.getDoctorAssignedResidents(doctorId)
        Assert.assertTrue(residents.isEmpty())
        every { mockIDoctorAssignedResidentsRepository.findById(doctorId) } returns
            Optional.of(DoctorAssignedResidentsBean(doctorId, listOf(3, 5)))
    }

    @Test
    fun getDoctorAssignedResidents_two() {
        every { mockIDoctorAssignedResidentsRepository.findById(doctorId) } returns
            Optional.of(DoctorAssignedResidentsBean(
                doctorId,
                listOf(resident1Id, resident2Id)))
        val residents = staffService.getDoctorAssignedResidents(doctorId)
        Assert.assertFalse(residents.isEmpty())
        Assert.assertEquals(
            "resident list does not match",
            listOf(Person.fromUser(resident1), Person.fromUser(resident2)),
            residents)
    }

    @Test
    fun getDoctorAssignedResidents_one_empty() {
        every { mockIDoctorAssignedResidentsRepository.findById(doctorId) } returns
            Optional.of(DoctorAssignedResidentsBean(doctorId, listOf(6)))
        val residents = staffService.getDoctorAssignedResidents(doctorId)
        Assert.assertTrue(residents.isEmpty())
    }

    @Test
    fun setDoctorAssignedResidents() {
        staffService.setDoctorAssignedResidents(doctorId, listOf())
        verify(exactly = 1) {
            mockIDoctorAssignedResidentsRepository.save(allAny<DoctorAssignedResidentsBean>()) }
        Assertions.assertEquals(doctorId, slot.captured.doctorId, "doctor id does not match")
        Assertions.assertEquals(listOf<Long>(), slot.captured.residentIds, "resident list does not match")
    }

    @Test
    fun setDoctorAssignedResidents_two() {
        staffService.setDoctorAssignedResidents(doctorId, listOf(resident1Id, resident2Id))
        verify(exactly = 1) {
            mockIDoctorAssignedResidentsRepository.save(allAny<DoctorAssignedResidentsBean>()) }
        Assertions.assertEquals(doctorId, slot.captured.doctorId, "doctor id does not match")
        Assertions.assertEquals(
            listOf(resident1Id, resident2Id),
            slot.captured.residentIds,
            "resident list does not match")
    }

    @Test
    fun setDoctorAssignedResidents_one() {
        staffService.setDoctorAssignedResidents(doctorId, listOf(resident1Id))
        verify(exactly = 1) {
            mockIDoctorAssignedResidentsRepository.save(allAny<DoctorAssignedResidentsBean>()) }
        Assertions.assertEquals(doctorId, slot.captured.doctorId, "doctor id does not match")
        Assertions.assertEquals(
            listOf(resident1Id),
            slot.captured.residentIds,
            "resident list does not match")
    }
}
