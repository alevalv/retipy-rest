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

package co.avaldes.retipy.domain.task.landmarks

import co.avaldes.retipy.domain.common.roi.Roi
import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation
import co.avaldes.retipy.domain.evaluation.automated.RetipyTask
import co.avaldes.retipy.domain.task.AbstractRESTTask
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.BodyInserters

class ClassificationTask(
    retipyUri: String,
    private val retipyEvaluation: RetipyEvaluation) : AbstractRESTTask<RetipyEvaluation>(
    RetipyTask.LandmarksClassification.name,
    "/landmarks/classification",
    retipyUri,
    emptyMap())
{
    private data class ClassificationTaskRequest(val image: String)
    private data class ClassificationTaskResponse(
        val bifurcations: List<List<Int>>, val crossings: List<List<Int>>)

    private val logger: Logger = LoggerFactory.getLogger(ClassificationTask::class.java)

    override fun execute(): RetipyEvaluation
    {
        logger.info("Starting Task")
        val requestWithBody = getRequest(HttpMethod.POST)
            .body(BodyInserters.fromObject(ClassificationTaskRequest(retipyEvaluation.image)))

        try {
            val response = requestWithBody.retrieve()
                .bodyToMono(ClassificationTaskResponse::class.java)
                .block()!!
            val rois: MutableList<Roi> = ArrayList()
            response.bifurcations.forEach {
                val x = ArrayList<Int>()
                x.add(it[0])
                x.add(it[0])
                x.add(it[2])
                x.add(it[2])
                val y = ArrayList<Int>()
                y.add(it[1])
                y.add(it[3])
                y.add(it[3])
                y.add(it[1])
                rois.add(Roi(x, y, "Bifurcation", "red")) }

            response.crossings.forEach {
                val x = ArrayList<Int>()
                x.add(it[0])
                x.add(it[0])
                x.add(it[2])
                x.add(it[2])
                val y = ArrayList<Int>()
                y.add(it[1])
                y.add(it[3])
                y.add(it[3])
                y.add(it[1])
                rois.add(Roi(x, y, "Crossing", "blue")) }
            retipyEvaluation.rois = rois
            retipyEvaluation.status = RetipyEvaluationStatus.Complete
            logger.info("Completed Successfully")
        }
        catch (exception: Exception)
        {
            // retipyEvaluation.status = RetipyEvaluationStatus.Error
            logger.info("Failed $exception")
        }
        return retipyEvaluation
    }
}
