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

import co.avaldes.retipy.common.nm.Education
import co.avaldes.retipy.common.nm.Sex
import co.avaldes.retipy.domain.evaluation.optical.OpticalEvaluation
import co.avaldes.retipy.persistence.patient.PatientBean
import java.util.*
import kotlin.collections.HashMap

data class Patient(
    var id: Long,
    var identity: Long,
    var name: String,
    var birthDate: Date,
    var sex: Sex,
    var origin: String,
    var procedence: String,
    var education: Education,
    var race: String,
    var pathologicalPast: List<String>,
    var familiarPast: List<String>,
    var medicines: List<String>,
    private val opticalEvaluations: List<OpticalEvaluation>)
{
    private val opticalEvaluationMap : MutableMap<Long, OpticalEvaluation> = HashMap()

    init
    {
        this.opticalEvaluations.forEach{
            opticalEvaluationMap[it.version] = it
        }
    }

    fun getMedicalRecords() : List<OpticalEvaluation> = opticalEvaluationMap.values.toList().sortedBy { record -> record.version }

    fun getMedicalRecord(id: Long) : OpticalEvaluation? = opticalEvaluationMap[id]

    fun setMedicalRecord(opticalEvaluation: OpticalEvaluation)
    {
        opticalEvaluationMap[opticalEvaluation.id] = opticalEvaluation
    }

    fun recordCount() = opticalEvaluationMap.size

    companion object
    {
        private const val SEPARATOR = "|"

        fun fromPersistence(patientBean: PatientBean) = Patient(
            patientBean.id,
            patientBean.identity,
            patientBean.name,
            patientBean.birthDate,
            patientBean.sex,
            patientBean.origin,
            patientBean.procedence,
            patientBean.education,
            patientBean.race,
            parseListFromString(patientBean.pathologicalPast),
            parseListFromString(patientBean.familiarPast),
            parseListFromString(patientBean.medicines),
            patientBean.opticalEvaluations.map { OpticalEvaluation.fromPersistence(it) })

        fun toPersistence(patient: Patient) = PatientBean(
            patient.id,
            patient.identity,
            patient.name,
            patient.birthDate,
            patient.sex,
            patient.origin,
            patient.procedence,
            patient.education,
            patient.race,
            parseListToString(patient.pathologicalPast),
            parseListToString(patient.familiarPast),
            parseListToString(patient.medicines),
            patient.getMedicalRecords().map { OpticalEvaluation.toPersistence(it) })
        
        private fun parseListFromString(string:String): List<String>
        {
            return if (string.isNotBlank()) string.split(SEPARATOR).toList() else emptyList()
        }

        private fun parseListToString(list:List<String>): String
        {
            val builder = StringBuilder()
            if (!list.isEmpty())
            {
                list.forEach {
                    builder.append(it)
                    builder.append(SEPARATOR)
                }
                builder.deleteCharAt(builder.lastIndexOf(SEPARATOR))
            }
            return builder.toString()
        }
    }
}
