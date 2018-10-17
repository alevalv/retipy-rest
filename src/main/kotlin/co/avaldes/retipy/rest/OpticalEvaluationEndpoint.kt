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

import co.avaldes.retipy.domain.evaluation.optical.IOpticalEvaluationService
import co.avaldes.retipy.domain.staff.IStaffAuditingService
import co.avaldes.retipy.persistence.staff.AuditingOperation
import co.avaldes.retipy.rest.dto.patient.OpticalEvaluationDTO
import co.avaldes.retipy.rest.dto.patient.OpticalEvaluationDTOMapper
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * Endpoint for retrieving [OpticalEvaluationDTO]
 */
@CrossOrigin
@RestController
class OpticalEvaluationEndpoint(
    private val opticalEvaluationService: IOpticalEvaluationService,
    private val auditingService: IStaffAuditingService,
    private val opticalEvaluationDTOMapper: OpticalEvaluationDTOMapper)
{
    @GetMapping("/retipy/opticalevaluation/{id}")
    fun getOpticalEvaluation(@PathVariable id: Long): OpticalEvaluationDTO
    {
        val opticalEvaluation = opticalEvaluationDTOMapper.fromDomain(opticalEvaluationService.get(id))
        auditingService.audit(opticalEvaluation.id, AuditingOperation.OpticalEvaluationRead)
        return opticalEvaluation
    }
}
