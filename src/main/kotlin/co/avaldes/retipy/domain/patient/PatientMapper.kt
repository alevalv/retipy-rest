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

package co.avaldes.retipy.domain.patient

import co.avaldes.retipy.domain.common.IMapper
import co.avaldes.retipy.domain.common.Person
import co.avaldes.retipy.domain.evaluation.optical.OpticalEvaluation
import co.avaldes.retipy.persistence.patient.PatientBean
import co.avaldes.retipy.security.domain.user.IUserService
import org.springframework.stereotype.Component

/**
 * Implementation of [IMapper] for [Patient] and [PatientBean].
 *
 * This mapper will query the userService to map the doctors associated with the patient.
 */
@Component
class PatientMapper(private val userService: IUserService) : IMapper<PatientBean, Patient> {
    private val SEPARATOR = "|"

    override fun toPersistence(domainObject: Patient): PatientBean {
        return PatientBean(
            domainObject.id,
            domainObject.identity,
            domainObject.name,
            domainObject.birthDate,
            domainObject.sex,
            domainObject.origin,
            domainObject.procedence,
            domainObject.education,
            domainObject.race,
            parseListToString(domainObject.pathologicalPast),
            parseListToString(domainObject.familiarPast),
            parseListToString(domainObject.medicines),
            domainObject.getOpticalEvaluations().map { OpticalEvaluation.toPersistence(it) },
            domainObject.assignedDoctors.map { it.id }
        )
    }

    override fun fromPersistence(bean: PatientBean): Patient {
        val doctors: List<Person> =
            bean.assignedDoctors
                .asSequence()
                .map { userService.get(it) }
                .map { Person(it.id, it.identity, it.name) }
                .toList()

        return Patient(
            bean.id,
            bean.identity,
            bean.name,
            bean.birthDate,
            bean.sex,
            bean.origin,
            bean.procedence,
            bean.education,
            bean.race,
            parseListFromString(bean.pathologicalPast),
            parseListFromString(bean.familiarPast),
            parseListFromString(bean.medicines),
            bean.opticalEvaluations.map { OpticalEvaluation.fromPersistence(it) },
            doctors
        )
    }

    private fun parseListFromString(string: String): List<String> {
        return if (string.isNotBlank()) string.split(SEPARATOR).toList() else emptyList()
    }

    private fun parseListToString(list: List<String>): String {
        val builder = StringBuilder()
        if (list.isNotEmpty()) {
            list.forEach {
                builder.append(it)
                builder.append(SEPARATOR)
            }
            builder.deleteCharAt(builder.lastIndexOf(SEPARATOR))
        }
        return builder.toString()
    }
}
