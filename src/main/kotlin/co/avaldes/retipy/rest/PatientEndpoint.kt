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

import co.avaldes.retipy.domain.patient.IPatientService
import co.avaldes.retipy.domain.patient.Patient
import co.avaldes.retipy.domain.staff.IStaffAuditingService
import co.avaldes.retipy.domain.staff.IStaffService
import co.avaldes.retipy.persistence.staff.AuditingOperation
import co.avaldes.retipy.rest.common.IncorrectInputException
import co.avaldes.retipy.rest.dto.DiagnosticDTO
import co.avaldes.retipy.rest.dto.patient.OpticalEvaluationDTO
import co.avaldes.retipy.rest.dto.patient.OpticalEvaluationDTOMapper
import co.avaldes.retipy.rest.dto.patient.PatientDTO
import co.avaldes.retipy.rest.dto.patient.PatientDTOMapper
import co.avaldes.retipy.rest.dto.patient.PersonDTO
import co.avaldes.retipy.security.domain.user.IUserService
import co.avaldes.retipy.security.domain.user.User
import co.avaldes.retipy.security.persistence.user.Role
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
    private val auditingService: IStaffAuditingService,
    private val userService: IUserService,
    private val staffService: IStaffService,
    private val opticalEvaluationDTOMapper: OpticalEvaluationDTOMapper,
    private val patientDTOMapper: PatientDTOMapper)
{
    data class PatientListDTO(val patientList: List<PersonDTO>)

    @GetMapping("/retipy/patient/{id}")
    fun getPatient(@PathVariable id: Long): PatientDTO
    {
        val patient = patientDTOMapper.fromDomain(patientService.get(id))
        auditingService.audit(id, AuditingOperation.PatientRead)
        return patient
    }

    @GetMapping("/retipy/patient/list")
    fun listPatient(): PatientListDTO
    {
        val user = getUser()
        return if (user.roles.contains(Role.Resident))
        {
            PatientListDTO(
                patientService
                    .getAllPatientsByDoctorIds(staffService.getDoctorsFromResident(user.id))
                    .map { PersonDTO.fromDomain(it) })
        }
        else
        {
            PatientListDTO(patientService.getAllPatients().map { PersonDTO.fromDomain(it) })
        }
    }

    @PostMapping("/retipy/patient")
    fun savePatient(@RequestBody patientDTO: PatientDTO) : PatientDTO
    {
        val patientToSave: Patient = patientDTOMapper.toDomain(patientDTO)
        if (getUser().roles.contains(Role.Resident))
        {
            // we can't allow a resident to change the doctors assigned to a patient
            // this also have an excellent side effect, a resident cannot create a new patient.
            val existingPatient = patientService.get(patientDTO.id)
            patientToSave.assignedDoctors = existingPatient.assignedDoctors
        }
        val savedPatient = patientDTOMapper.fromDomain(patientService.save(patientToSave))
        auditingService.audit(savedPatient.id, AuditingOperation.PatientEdit)
        return savedPatient
    }

    @PostMapping("/retipy/patient/{id}/opticalevaluation")
    fun saveOpticalEvaluation(@PathVariable id: Long, @RequestBody opticalEvaluationDTO: OpticalEvaluationDTO): OpticalEvaluationDTO
    {
        val savedOpticalEvaluation = opticalEvaluationDTOMapper.fromDomain(
            patientService.saveOpticalEvaluation(
                id, opticalEvaluationDTOMapper.toDomain(opticalEvaluationDTO)))
        auditingService.audit(savedOpticalEvaluation.id,AuditingOperation.OpticalEvaluationEdit)
        return savedOpticalEvaluation
    }

    @PostMapping("/retipy/patient/{patientId}/opticalevaluation/{opticalEvaluationId}/diagnostic")
    fun saveDiagnostic(
        @PathVariable patientId: Long,
        @PathVariable opticalEvaluationId: Long,
        @RequestBody diagnosticDTO: DiagnosticDTO): DiagnosticDTO
    {
        val savedDiagnostic = DiagnosticDTO.fromDomain(
            patientService.saveDiagnostic(
                patientId, opticalEvaluationId, DiagnosticDTO.toDomain(diagnosticDTO)))
        auditingService.audit(savedDiagnostic.id, AuditingOperation.DiagnosticEdit)
        return savedDiagnostic
    }

    @PostMapping(
        "/retipy/patient/{patientId}/opticalevaluation/{opticalEvaluationId}/diagnostic/image",
        consumes = [MediaType.TEXT_PLAIN_VALUE])
    fun saveDiagnosticByImage(
        @PathVariable patientId: Long,
        @PathVariable opticalEvaluationId: Long,
        @RequestBody image: String): DiagnosticDTO
    {
        val savedDiagnostic = DiagnosticDTO.fromDomain(
            patientService.saveDiagnosticByImage(patientId, opticalEvaluationId, image))
        auditingService.audit(savedDiagnostic.id, AuditingOperation.DiagnosticEdit)
        return savedDiagnostic
    }

    private fun getUser(): User =
        userService.getCurrentAuthenticatedUser() ?: throw IncorrectInputException("User should exist")
}
