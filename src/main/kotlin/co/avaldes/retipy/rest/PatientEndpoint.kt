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
import co.avaldes.retipy.rest.dto.patient.PatientDTO
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
internal class PatientEndpoint(private val patientService: IPatientService)
{
    @GetMapping("/retipy/patient/{id}")
    fun getPatient(@PathVariable id: Long): PatientDTO
    {
        return PatientDTO.fromDomain(patientService.get(id))
    }

    @GetMapping("/retipy/patient/list")
    fun listPatient(): List<Triple<Long, Long, String>>
    {
        return patientService.getAllPatients()
    }

    @PostMapping("/retipy/patient")
    fun savePatient(@RequestBody patientDTO: PatientDTO) : PatientDTO
    {
        return PatientDTO.fromDomain(patientService.save(PatientDTO.toDomain(patientDTO)))
    }

}
