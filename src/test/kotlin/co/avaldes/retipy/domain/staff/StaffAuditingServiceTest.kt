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

import co.avaldes.retipy.persistence.staff.IStaffAccessAuditingRepository
import co.avaldes.retipy.persistence.staff.AuditingOperation
import co.avaldes.retipy.persistence.staff.StaffAccessAuditingBean
import co.avaldes.retipy.security.domain.user.IUserService
import co.avaldes.retipy.security.domain.user.User
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

internal class StaffAuditingServiceTest
{
    private val resourceId  = 109083L
    private val userId = 2308L
    private val username = "a username"
    private val date = Date.from(Instant.EPOCH)
    private val savedBean =
        StaffAccessAuditingBean(
            1L, resourceId, AuditingOperation.DiagnosticDelete, userId, username, date)
    private val user = User(userId, "193123", "a name", username, "ui1312")

    private val mockAuditingRepository: IStaffAccessAuditingRepository = mockk(relaxed = true)
    private val mockUserService : IUserService = mockk(relaxed = true)
    private val slot = slot<StaffAccessAuditingBean>()

    private lateinit var staffAuditingService: StaffAuditingService

    @BeforeEach
    fun setUp()
    {
        clearMocks(mockAuditingRepository, mockUserService)
        every {
            mockAuditingRepository.save(capture(slot))
        } returns savedBean
        every {
            mockAuditingRepository.findByUserId(userId)
        } returns listOf(savedBean)
        every {
            mockAuditingRepository.findByResourceId(resourceId)
        } returns listOf(savedBean)
        every { mockUserService.getCurrentAuthenticatedUser() } returns user
        staffAuditingService = StaffAuditingService(mockAuditingRepository, mockUserService)
    }

    @Test
    fun log_userInput()
    {
        val auditingOperation = AuditingOperation.DiagnosticDelete
        staffAuditingService.audit(resourceId, auditingOperation, userId, username)
        verify(exactly = 1) { mockAuditingRepository.save(allAny<StaffAccessAuditingBean>()) }
        Assert.assertEquals("user id does not match", userId, slot.captured.userId)
        Assert.assertNotEquals("date should not match", savedBean.date, slot.captured.date)
        Assert.assertNotEquals("id should not match", savedBean.id, slot.captured.id)
        Assert.assertEquals(
            "auditing operation does not match", auditingOperation, slot.captured.auditingOperation)
    }

    @Test
    fun log_currentUser()
    {
        val auditingOperation = AuditingOperation.DiagnosticRead
        staffAuditingService.audit(resourceId, auditingOperation)
        verify(exactly = 1) { mockAuditingRepository.save(allAny<StaffAccessAuditingBean>()) }
        Assert.assertEquals("user id does not match", userId, slot.captured.userId)
        Assert.assertNotEquals("date should not match", savedBean.date, slot.captured.date)
        Assert.assertNotEquals("id should not match", savedBean.id, slot.captured.id)
        Assert.assertEquals(
            "auditing operation does not match", auditingOperation, slot.captured.auditingOperation)
    }

    @Test
    fun getLogsByUser()
    {
        val logs = staffAuditingService.getLogsByUser(userId)

        Assertions.assertEquals(
            1,
            logs.size,
            "only one audit message expected")
        Assertions.assertEquals(
            "[Diagnostic Deleted - 109083]:$date - user:a username",
            logs.first(),
            "audit does not match")
    }

    @Test
    fun getLogsByResource()
    {
        val logs = staffAuditingService.getLogsByResource(resourceId)

        Assertions.assertEquals(
            1,
            logs.size,
            "only one audit message expected")
        Assertions.assertEquals(
            "[Diagnostic Deleted - 109083]:$date - user:a username",
            logs.first(),
            "audit does not match")
    }
}
