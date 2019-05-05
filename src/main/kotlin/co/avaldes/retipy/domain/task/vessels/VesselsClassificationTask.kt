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

package co.avaldes.retipy.domain.task.vessels

import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation
import co.avaldes.retipy.domain.evaluation.automated.RetipyTask
import co.avaldes.retipy.domain.task.AbstractRESTTask
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.BodyInserters

class VesselsClassificationTask(
    retipyUri: String,
    configuration: Map<String, String> = mapOf(),
    private val retipyEvaluation: RetipyEvaluation,
    private val segmentedRetipyEvaluation: RetipyEvaluation?
) : AbstractRESTTask<RetipyEvaluation>(
        RetipyTask.VesselsClassification.name,
        "/vessel_classification",
        retipyUri,
        mapOf()
    ) {
    private data class VesselsClassificationTaskRequest(val original_image: String, val segmented_image: String)
    private data class VesselsClassificationTaskResponse(val classification: String)

    private val logger: Logger = LoggerFactory.getLogger(VesselsClassificationTask::class.java)
    private val parameters = addMissingProperties(configuration)

    override fun execute(): RetipyEvaluation {
        logger.info("Starting task")
        if (segmentedRetipyEvaluation == null) {
            retipyEvaluation.status = RetipyEvaluationStatus.Error
            logger.info("Retipy evaluation with id {} has no segmentation", retipyEvaluation.diagnosticId)
            return retipyEvaluation
        }

        val requestWithBody = getRequest(
            HttpMethod.POST, uri = this.uri)
            .body(BodyInserters.fromObject(
                    VesselsClassificationTaskRequest(retipyEvaluation.image, segmentedRetipyEvaluation.image)))

        try {
            val response = requestWithBody.retrieve()
                .bodyToMono(VesselsClassificationTaskResponse::class.java)
                .block()!!
            retipyEvaluation.image = response.classification
            retipyEvaluation.status = RetipyEvaluationStatus.Complete
            logger.info("Completed")
        } catch (e: Exception) {
            retipyEvaluation.status = RetipyEvaluationStatus.Error
            logger.info("Failed $e")
        }

        return retipyEvaluation
    }
}
