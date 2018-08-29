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

import co.avaldes.retipy.domain.record.IPatientService
import co.avaldes.retipy.rest.dto.DiagnosticDTO
import co.avaldes.retipy.rest.dto.patient.OpticalEvaluationDTO
import co.avaldes.retipy.rest.dto.patient.OpticalEvaluationDTOMapper
import co.avaldes.retipy.rest.dto.patient.PatientDTO
import co.avaldes.retipy.rest.dto.patient.PatientDTOMapper
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * Endpoint for patient related operations.
 */
@CrossOrigin
@RestController
internal class PatientEndpoint(
    private val patientService: IPatientService,
    private val opticalEvaluationDTOMapper: OpticalEvaluationDTOMapper,
    private val patientDTOMapper: PatientDTOMapper)
{
    data class PatientListDTO(val patientList: List<Triple<Long, Long, String>>)

    @GetMapping("/retipy/patient/{id}")
    fun getPatient(@PathVariable id: Long): PatientDTO
    {
        return patientDTOMapper.fromDomain(patientService.get(id))
    }

    @GetMapping("/retipy/patient/list")
    fun listPatient(): PatientListDTO
    {
        return PatientListDTO(patientService.getAllPatients())
    }

    @PostMapping("/retipy/patient")
    fun savePatient(@RequestBody patientDTO: PatientDTO) : PatientDTO
    {
        return patientDTOMapper.fromDomain(
            patientService.save(patientDTOMapper.toDomain(patientDTO)))
    }

    @PostMapping("/retipy/patient/{id}/opticalevaluation")
    fun saveOpticalEvaluation(@PathVariable id: Long, @RequestBody opticalEvaluationDTO: OpticalEvaluationDTO): OpticalEvaluationDTO
    {
        return opticalEvaluationDTOMapper.fromDomain(
            patientService.saveOpticalEvaluation(
                id, opticalEvaluationDTOMapper.toDomain(opticalEvaluationDTO)))
    }

    @PostMapping("/retipy/patient/{patientId}/opticalevaluation/{opticalEvaluationId}/diagnostic")
    fun saveDiagnostic(
        @PathVariable patientId: Long,
        @PathVariable opticalEvaluationId: Long,
        @RequestBody diagnosticDTO: DiagnosticDTO): DiagnosticDTO {
        return DiagnosticDTO.fromDomain(
            patientService.saveDiagnostic(
                patientId, opticalEvaluationId, DiagnosticDTO.toDomain(diagnosticDTO)))
    }

    @PostMapping(
        "/retipy/patient/{patientId}/opticalevaluation/{opticalEvaluationId}/diagnostic/image",
        consumes = [MediaType.TEXT_PLAIN_VALUE])
    fun saveDiagnosticByImage(
        @PathVariable patientId: Long,
        @PathVariable opticalEvaluationId: Long,
        @RequestBody image: String): DiagnosticDTO {
        return DiagnosticDTO.fromDomain(
            patientService.saveDiagnosticByImage(patientId, opticalEvaluationId, image))
    }
}
