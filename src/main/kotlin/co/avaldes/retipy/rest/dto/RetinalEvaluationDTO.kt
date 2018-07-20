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

import co.avaldes.retipy.domain.evaluation.retinal.RetinalEvaluation
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import java.util.*


@JsonRootName("evaluation")
data class RetinalEvaluationDTO(
    var id: Long?,
    val uri: String,
    val timestamp: Date?,
    @JsonIgnore
        var status: RetinalEvaluation.EvaluationStatus?,
    val results: List<ResultDTO>)
{
    companion object
    {
        fun fromDomain(retinalEvaluation: RetinalEvaluation) = RetinalEvaluationDTO(
                id = retinalEvaluation.id,
                uri = retinalEvaluation.uri,
                timestamp = retinalEvaluation.timestamp,
                status = retinalEvaluation.status,
                results = ResultDTO.toList(retinalEvaluation.results))

        fun toDomain(retinalEvaluationDTO: RetinalEvaluationDTO) = RetinalEvaluation(
            id = retinalEvaluationDTO.id ?: 0,
            uri = retinalEvaluationDTO.uri,
            timestamp = retinalEvaluationDTO.timestamp ?: Date(),
            status = retinalEvaluationDTO.status
                ?: RetinalEvaluation.EvaluationStatus.PENDING,
            results = ResultDTO.fromList(retinalEvaluationDTO.results))
    }
}
