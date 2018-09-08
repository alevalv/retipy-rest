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

package co.avaldes.retipy.rest.dto

import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation
import co.avaldes.retipy.persistence.evaluation.retinal.RetinalEvaluationStatus
import java.util.*

data class RetipyEvaluationDTO(
    val id: Long,
    val diagnosticId: Long,
    val name: String,
    val image: String,
    val rois: List<RoiDTO>,
    val status: RetinalEvaluationStatus,
    val creationDate: Date,
    val updateDate: Date)
{
    companion object
    {
        fun fromDomain(retipyEvaluation: RetipyEvaluation) = RetipyEvaluationDTO(
            retipyEvaluation.id,
            retipyEvaluation.diagnosticId,
            retipyEvaluation.name,
            retipyEvaluation.image,
            retipyEvaluation.rois.map { RoiDTO.fromDomain(it) },
            retipyEvaluation.status,
            retipyEvaluation.creationDate,
            retipyEvaluation.updateDate
        )

        fun toDomain(retipyEvaluationDTO: RetipyEvaluationDTO) = RetipyEvaluation(
            retipyEvaluationDTO.id,
            retipyEvaluationDTO.diagnosticId,
            retipyEvaluationDTO.name,
            retipyEvaluationDTO.image,
            retipyEvaluationDTO.rois.map { RoiDTO.toDomain(it) },
            retipyEvaluationDTO.status,
            retipyEvaluationDTO.creationDate,
            retipyEvaluationDTO.updateDate
        )
    }
}
