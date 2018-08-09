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

import co.avaldes.retipy.common.ICRUDService
import co.avaldes.retipy.domain.evaluation.optical.OpticalEvaluation


interface IPatientService: ICRUDService<Patient>
{
    /**
     * Add a new opticalEvaluation to the given [patientId], the opticalEvaluation will be placed last.
     */
    fun addRecordToPatient(patientId: Long, opticalEvaluation: OpticalEvaluation): Patient

    /**
     * Returns a list of triplets with the patient unique id, its identity and its name, sorted by
     * their identity.
     */
    fun getAllPatients(): List<Triple<Long, Long, String>>
}
