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


interface IPatientService
{
    /**
     * Gets the patient with the given [id]. If the patient does not exist. an
     * [IllegalArgumentException] will be thrown.
     */
    fun getPatient(id: Long) : Patient

    /**
     * Persists the given [patient] in the database. Returns a new [Patient] with possible changes
     * from the persistence layer.
     */
    fun save(patient: Patient) : Patient

    /**
     * Add a new record to the given [patientId], the record will be placed last.
     */
    fun addRecordToPatient(patientId: Long, record: Record): Patient
}
