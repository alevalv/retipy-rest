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

package co.avaldes.retipy.domain.evaluation.retinal

import co.avaldes.retipy.domain.Results
import co.avaldes.retipy.persistence.evaluation.retinal.RetinalEvaluationBean
import java.util.*

data class RetinalEvaluation(
    val id: Long,
    var uri: String,
    var timestamp: Date,
    val results: Results,
    var status: EvaluationStatus)
{
    enum class EvaluationStatus
    {
        PENDING, COMPLETE, ERROR
    }

    override fun toString(): String = "Evaluation($id, $uri)"

    companion object
    {
        fun toPersistence(retinalEvaluation: RetinalEvaluation) = RetinalEvaluationBean(
            retinalEvaluation.id,
            retinalEvaluation.uri,
            retinalEvaluation.timestamp,
            retinalEvaluation.results.getResults(),
            retinalEvaluation.status)

        fun fromPersistence(retinalEvaluationBean: RetinalEvaluationBean) = RetinalEvaluation(
            retinalEvaluationBean.id,
            retinalEvaluationBean.uri,
            retinalEvaluationBean.timestamp,
            Results(retinalEvaluationBean.results),
            retinalEvaluationBean.status)
    }
}
