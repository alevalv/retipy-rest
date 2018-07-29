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
import org.springframework.stereotype.Service

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
        find(id) ?: throw IllegalArgumentException("patient with id $id not found")

    override fun save(obj: Patient) : Patient
    {
        val savedPatient = patientRepository.save(Patient.toPersistence(obj))
        return Patient.fromPersistence(savedPatient)
    }

    override fun addRecordToPatient(patientId: Long, opticalEvaluation: OpticalEvaluation): Patient
    {
        val savedPatient = get(patientId)
        opticalEvaluation.id = savedPatient.recordCount().toLong()
        savedPatient.setMedicalRecord(opticalEvaluation)
        return save(savedPatient)
    }
}
