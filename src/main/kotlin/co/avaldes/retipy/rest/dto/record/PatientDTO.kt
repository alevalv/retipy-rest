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

package co.avaldes.retipy.rest.dto.record

import co.avaldes.retipy.common.nm.Education
import co.avaldes.retipy.common.nm.Sex
import co.avaldes.retipy.domain.record.Patient
import java.util.*


data class PatientDTO(
    val id: Long,
    val identity: Long,
    val name: String,
    val birthDate: Date,
    val sex: Sex,
    val origin: String,
    val procedence: String,
    val education: Education,
    val race: String,
    val pathologicalPast: List<String>,
    val familiarPast: List<String>,
    val medicines: List<String>,
    val records: List<RecordDTO>)
{
    companion object
    {
        fun fromDomain(patient: Patient) = PatientDTO(
            patient.id,
            patient.identity,
            patient.name,
            patient.birthDate,
            patient.sex,
            patient.origin,
            patient.procedence,
            patient.education,
            patient.race,
            patient.pathologicalPast,
            patient.familiarPast,
            patient.medicines,
            patient.getMedicalRecords().map { RecordDTO.fromDomain(it) })

        fun toDomain(patientDTO: PatientDTO) = Patient(
            patientDTO.id,
            patientDTO.identity,
            patientDTO.name,
            patientDTO.birthDate,
            patientDTO.sex,
            patientDTO.origin,
            patientDTO.procedence,
            patientDTO.education,
            patientDTO.race,
            patientDTO.pathologicalPast,
            patientDTO.familiarPast,
            patientDTO.medicines,
            patientDTO.records.map { RecordDTO.toDomain(it) })
    }
}
