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

package co.avaldes.retipy.rest

import co.avaldes.retipy.domain.diagnostic.IDiagnosticService
import co.avaldes.retipy.domain.staff.IStaffAuditingService
import co.avaldes.retipy.persistence.staff.AuditingOperation
import co.avaldes.retipy.rest.dto.DiagnosticDTO
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * Endpoint for Diagnostics.
 * Only get operations are allowed here, saving and deleting are responsibilities of patient.
 */
@CrossOrigin
@RestController
internal class DiagnosticEndpoint(
    private val diagnosticService: IDiagnosticService,
    private val auditingService: IStaffAuditingService)
{
    @GetMapping("/retipy/diagnostic/{id}")
    fun getPatient(@PathVariable id: Long): DiagnosticDTO
    {
        val diagnostic = DiagnosticDTO.fromDomain(diagnosticService.get(id))
        auditingService.audit(diagnostic.id, AuditingOperation.DiagnosticRead)
        return diagnostic
    }
}
