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

package co.avaldes.retipy.domain.record

import co.avaldes.retipy.domain.evaluation.optical.OpticalEvaluation
import co.avaldes.retipy.persistence.patient.IPatientRepository
import co.avaldes.retipy.rest.common.IncorrectInputException
import org.springframework.stereotype.Service
import java.util.*

@Service
class PatientService(val patientRepository: IPatientRepository) : IPatientService
{
    override fun find(id: Long): Patient?
    {
        var patient : Patient? = null
        val savedPatientBean = patientRepository.findById(id)
        if (savedPatientBean.isPresent)
            patient = Patient.fromPersistence(savedPatientBean.get())
        return patient
    }

    override fun delete(obj: Patient)
    {
        patientRepository.delete(Patient.toPersistence(obj))
    }

    override fun delete(id: Long)
    {
        patientRepository.deleteById(id)
    }

    override fun get(id: Long) : Patient =
        find(id) ?: throw IncorrectInputException("patient with id $id not found")

    override fun save(obj: Patient) : Patient
    {
        if (obj.id == 0L)
        {
            if (patientRepository.findByIdentity(obj.identity) != null)
            {
                throw IncorrectInputException(
                    "Patient with identity ${obj.identity} already exists")
            }
        }
        val savedPatient = patientRepository.save(Patient.toPersistence(obj))
        return Patient.fromPersistence(savedPatient)
    }

    override fun createOpticalEvaluation(patientId: Long): OpticalEvaluation =
        saveOpticalEvaluation(patientId, OpticalEvaluation())

    override fun saveOpticalEvaluation(patientId: Long, opticalEvaluation: OpticalEvaluation): OpticalEvaluation
    {
        val savedPatient = get(patientId)
        val persistedIds = savedPatient.getOpticalEvaluations().map { it -> it.id }
        savedPatient.addOpticalEvaluation(opticalEvaluation)
        val persistedPatient = save(savedPatient)
        return if (opticalEvaluation.id == 0L)
            persistedPatient.getOpticalEvaluations().filterNot {persistedIds.contains(it.id)}.first()
            else persistedPatient.getOpticalEvaluation(opticalEvaluation.id)!!
    }

    override fun getAllPatients(): List<Triple<Long, Long, String>>
    {
        val patientsBeans = patientRepository.findAll()
        val patientList: MutableList<Triple<Long, Long, String>> = ArrayList(patientsBeans.count())
        patientsBeans
            .sortedBy { patient -> patient.identity }
            .forEach{ patient -> patientList.add(Triple(patient.id, patient.identity, patient.name)) }

        return patientList
    }
}
