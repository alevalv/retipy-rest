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

package co.avaldes.retipy.domain.evaluation.automated

import co.avaldes.retipy.domain.common.roi.Roi
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationBean
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import java.util.*
import kotlin.collections.ArrayList

/**
 * Class that represents a automated evaluation performed by the retipy backend.
 */
data class RetipyEvaluation(
    var id: Long = 0L,
    var diagnosticId: Long,
    var name: RetipyTask = RetipyTask.None,
    var image: String = "",
    var rois: List<Roi> = ArrayList(),
    var status: RetipyEvaluationStatus = RetipyEvaluationStatus.Pending,
    var creationDate: Date = Date(),
    var updateDate: Date = Date()
)
{
    companion object
    {
        fun toPersistence(retipyEvaluation: RetipyEvaluation) = RetipyEvaluationBean(
            retipyEvaluation.id,
            retipyEvaluation.diagnosticId,
            retipyEvaluation.name.name,
            retipyEvaluation.image,
            Roi.toPersistence(retipyEvaluation.rois),
            retipyEvaluation.status,
            retipyEvaluation.creationDate,
            retipyEvaluation.updateDate
        )

        fun fromPersistence(retipyEvaluationBean: RetipyEvaluationBean) = RetipyEvaluation(
            retipyEvaluationBean.id,
            retipyEvaluationBean.diagnosticId,
            RetipyTask.valueOf(retipyEvaluationBean.name),
            retipyEvaluationBean.image,
            Roi.fromPersistence(retipyEvaluationBean.rois),
            retipyEvaluationBean.status,
            retipyEvaluationBean.creationDate,
            retipyEvaluationBean.updateDate
        )
    }
}
