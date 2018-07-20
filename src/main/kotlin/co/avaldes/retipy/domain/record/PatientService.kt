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
    override fun getPatient(id: Long) : Patient
    {
        val savedPatientBean = patientRepository.findById(id)
        if (!savedPatientBean.isPresent)
            throw IllegalArgumentException("patient with id $id not found")
        return Patient.fromPersistence(savedPatientBean.get())
    }

    override fun save(patient: Patient) : Patient
    {
        val savedPatient = patientRepository.save(Patient.toPersistence(patient))
        return Patient.fromPersistence(savedPatient)
    }

    override fun addRecordToPatient(patientId: Long, opticalEvaluation: OpticalEvaluation): Patient
    {
        val savedPatient = getPatient(patientId)
        opticalEvaluation.id = savedPatient.recordCount().toLong()
        savedPatient.setMedicalRecord(opticalEvaluation)
        return save(savedPatient)
    }
}
