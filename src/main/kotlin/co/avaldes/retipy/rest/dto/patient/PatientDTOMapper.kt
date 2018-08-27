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

package co.avaldes.retipy.rest.dto.patient

import co.avaldes.retipy.domain.record.Patient
import co.avaldes.retipy.rest.common.IDTOMapper
import org.springframework.stereotype.Component

@Component
class PatientDTOMapper(
    private val opticalEvaluationDTOMapper: OpticalEvaluationDTOMapper): IDTOMapper<Patient, PatientDTO>
{
    override fun fromDomain(domainObject: Patient) = PatientDTO(
        domainObject.id,
        domainObject.identity,
        domainObject.name,
        domainObject.birthDate,
        domainObject.sex,
        domainObject.origin,
        domainObject.procedence,
        domainObject.education,
        domainObject.race,
        domainObject.pathologicalPast,
        domainObject.familiarPast,
        domainObject.medicines,
        domainObject.getOpticalEvaluations().map { opticalEvaluationDTOMapper.fromDomain(it) }
    )

    override fun toDomain(dto: PatientDTO) = Patient(
        dto.id,
        dto.identity,
        dto.name,
        dto.birthDate,
        dto.sex,
        dto.origin,
        dto.procedence,
        dto.education,
        dto.race,
        dto.pathologicalPast,
        dto.familiarPast,
        dto.medicines,
        dto.opticalEvaluations.map { opticalEvaluationDTOMapper.toDomain(it) }
    )
}
