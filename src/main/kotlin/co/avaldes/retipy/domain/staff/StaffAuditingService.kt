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
import co.avaldes.retipy.rest.common.IncorrectInputException
import co.avaldes.retipy.security.domain.user.IUserService
import org.springframework.stereotype.Service
import java.util.*

/**
 * Service that allow registering the access of users to certain operations and data, for auditing
 * purposes.
 */
@Service
internal class StaffAuditingService(
    private val staffAccessAuditingRepository: IStaffAccessAuditingRepository,
    private val userService: IUserService
) : IStaffAuditingService
{
    override fun audit(
        resourceId: Long, auditingOperation: AuditingOperation, userId: Long, username: String)
    {
        val bean = StaffAccessAuditingBean(0, resourceId, auditingOperation, userId, username, Date())
        staffAccessAuditingRepository.save(bean)
    }

    override fun audit(resourceId: Long, auditingOperation: AuditingOperation)
    {
        val user = userService.getCurrentAuthenticatedUser()
            ?: throw IncorrectInputException("User must be authenticated to be logged")
        audit(resourceId, auditingOperation, user.id, user.username)
    }

    override fun getLogsByResource(resourceId: Long): List<String>
    {
        return staffAccessAuditingRepository.findByResourceId(resourceId).map { logBeanToString(it) }
    }

    override fun getLogsByUser(userId: Long): List<String>
    {
        return staffAccessAuditingRepository.findByUserId(userId).map { logBeanToString(it) }
    }

    private fun logBeanToString(bean: StaffAccessAuditingBean): String =
        "[${bean.auditingOperation.operation} - ${bean.resourceId}]:${bean.date} - user:${bean.username}"
}
