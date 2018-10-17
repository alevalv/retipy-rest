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

import co.avaldes.retipy.common.ICRUDService
import co.avaldes.retipy.domain.common.Person
import co.avaldes.retipy.domain.diagnostic.Diagnostic
import co.avaldes.retipy.domain.evaluation.optical.OpticalEvaluation


interface IPatientService: ICRUDService<Patient>
{
    /**
     * Add a new opticalEvaluation to the given [patientId], the opticalEvaluation will be placed last.
     */
    fun saveOpticalEvaluation(patientId: Long, opticalEvaluation: OpticalEvaluation): OpticalEvaluation

    /**
     * Returns a list of [Person] the patient unique id, its identity and its name, sorted by
     * their identity.
     */
    fun getAllPatients(): List<Person>

    /**
     * Returns a list of [Person] with the patient information, filtered by the assignedDoctors
     * that has any value present in the given [doctorIds].
     */
    fun getAllPatientsByDoctorIds(doctorIds: List<Long>): List<Person>

    /**
     * Adds a new optical evaluation to the given patient.
     */
    fun createOpticalEvaluation(patientId: Long): OpticalEvaluation

    /**
     * Adds or modify the given diagnostic.
     * Will throw error if the [Patient] or the [OpticalEvaluation] does not exist.
     */
    fun saveDiagnostic(
        patientId: Long, opticalEvaluationId: Long, diagnostic: Diagnostic): Diagnostic

    /**
     * Creates a new diagnostic for the given image in base 64.
     * * Will throw error if the [Patient] or the [OpticalEvaluation] does not exist.
     */
    fun saveDiagnosticByImage(patientId: Long, opticalEvaluationId: Long, image: String): Diagnostic
}
