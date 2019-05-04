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

import co.avaldes.retipy.common.Education
import co.avaldes.retipy.common.Sex
import co.avaldes.retipy.domain.common.Person
import co.avaldes.retipy.domain.evaluation.optical.OpticalEvaluation
import java.util.Date

data class Patient(
    var id: Long,
    var identity: String,
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
    private val opticalEvaluations: List<OpticalEvaluation>,
    var assignedDoctors: List<Person>
) {
    private val opticalEvaluationMap: MutableMap<Long, OpticalEvaluation> = HashMap()

    init {
        this.opticalEvaluations.forEach {
            opticalEvaluationMap[it.id] = it
        }
    }

    fun getOpticalEvaluations(): List<OpticalEvaluation> = opticalEvaluationMap.values.toList().sortedBy { record -> record.id }

    fun getOpticalEvaluation(id: Long): OpticalEvaluation? = opticalEvaluationMap[id]

    fun addOpticalEvaluation(opticalEvaluation: OpticalEvaluation) {
        val existingOpticalEvaluation = opticalEvaluationMap[opticalEvaluation.id]
        if (existingOpticalEvaluation != null) {
            opticalEvaluation.version = existingOpticalEvaluation.version + 1L
        }
        opticalEvaluationMap[opticalEvaluation.id] = opticalEvaluation
    }

    fun opticalEvaluationCount() = opticalEvaluationMap.size
}
