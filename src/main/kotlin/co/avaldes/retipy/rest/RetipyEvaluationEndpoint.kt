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
import co.avaldes.retipy.domain.evaluation.automated.IRetipyEvaluationService
import co.avaldes.retipy.domain.evaluation.automated.RetipyTask
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import co.avaldes.retipy.rest.common.IncorrectInputException
import co.avaldes.retipy.rest.dto.RetipyEvaluationDTO
import co.avaldes.retipy.security.persistence.user.ROLE_ADMINISTRATOR
import co.avaldes.retipy.security.persistence.user.ROLE_DOCTOR
import co.avaldes.retipy.security.persistence.user.ROLE_WORKER
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * Endpoint that implements a REST CRUD for [RetipyEvaluationDTO].
 *
 * The rest uri is defined as [/retipy/evaluation] to retrieve an specific evaluation. Most
 * operations require a valid diagnostic id.
 */
@CrossOrigin
@RestController
internal class RetipyEvaluationEndpoint(
    private val retipyEvaluationService: IRetipyEvaluationService,
    private val diagnosticService: IDiagnosticService)
{
    data class RetipyEvaluationBasicDTO(val id: Long, val name: String, val status: RetipyEvaluationStatus)

    @GetMapping("/retipy/evaluation/{id}")
    fun getEvaluation(@PathVariable id: Long): RetipyEvaluationDTO
    {
        val evaluation = retipyEvaluationService.find(id)
            ?: throw  ResourceNotFoundException("$id is not a valid evaluation")
        return RetipyEvaluationDTO.fromDomain(evaluation)
    }

    @Secured(ROLE_WORKER)
    @GetMapping("/retipy/evaluation/Pending")
    fun getPendingEvaluations(): List<RetipyEvaluationDTO> =
        retipyEvaluationService.getPendingEvaluations().map { RetipyEvaluationDTO.fromDomain(it) }

    @GetMapping("/retipy/diagnostic/{id}/evaluation")
    fun getEvaluationsByDiagnostic(@PathVariable id: Long): List<RetipyEvaluationBasicDTO> =
        retipyEvaluationService
            .findByDiagnostic(id)
            .map { RetipyEvaluationBasicDTO(it.id, it.name.name, it.status) }

    @PostMapping("/retipy/diagnostic/{id}/evaluation")
    fun createNewEvaluation(@PathVariable id: Long, @RequestBody name: String)
    {
        try
        {
            retipyEvaluationService.fromDiagnostic(id, RetipyTask.valueOf(name))
        }
        catch (illegalArgumentException: IllegalArgumentException)
        {
            throw IncorrectInputException("cannot process request")
        }
    }

    @Secured(ROLE_WORKER)
    @PostMapping("/retipy/evaluation")
    fun saveEvaluation(@Valid @RequestBody evaluationDTO: RetipyEvaluationDTO): RetipyEvaluationDTO
    {
        return RetipyEvaluationDTO.fromDomain(
            retipyEvaluationService.save(RetipyEvaluationDTO.toDomain(evaluationDTO)))
    }

    @Secured(ROLE_DOCTOR, ROLE_ADMINISTRATOR)
    @DeleteMapping("/retipy/evaluation/{id}")
    fun deleteEvaluation(@PathVariable id: Long)
    {
        retipyEvaluationService.delete(id)
    }
}
