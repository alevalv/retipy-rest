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

import co.avaldes.retipy.persistence.staff.AuditingOperation

/**
 * Interface that implements logging operations for auditing purposes of retipy.
 */
interface IStaffAuditingService
{
    /**
     * Logs an operation for the given [userId] and [username].
     */
    fun audit(resourceId: Long, auditingOperation: AuditingOperation, userId: Long, username: String)

    /**
     * Logs an operation for the current logged user.
     */
    fun audit(resourceId: Long, auditingOperation: AuditingOperation)

    /**
     * Get the list of logs of the given [resourceId].
     */
    fun getLogsByResource(resourceId: Long): List<String>

    /**
     * Get the list of logs of the given [userId].
     */
    fun getLogsByUser(userId: Long): List<String>
}
