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

import co.avaldes.retipy.domain.diagnostic.IDiagnosticService
import co.avaldes.retipy.persistence.evaluation.retinal.IRetipyEvaluationRepository
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import co.avaldes.retipy.rest.common.IncorrectInputException
import org.springframework.stereotype.Service

/**
 * Domain operations for [RetipyEvaluation].
 *
 * This class implements the interface [IRetipyEvaluationService] which exposes the API.
 */
@Service
internal class RetipyEvaluationService(
    private val retipyEvaluationRepository: IRetipyEvaluationRepository,
    private val diagnosticService: IDiagnosticService)
    : IRetipyEvaluationService
{
    override fun find(id: Long): RetipyEvaluation?
    {
        val bean = retipyEvaluationRepository.findById(id)
        var retipyEvaluation: RetipyEvaluation? = null
        if (bean.isPresent)
            retipyEvaluation = RetipyEvaluation.fromPersistence(bean.get())
        return retipyEvaluation
    }

    override fun get(id: Long): RetipyEvaluation
    {
        return find(id)
            ?: throw IncorrectInputException("automated evaluation with id $id does not exist")
    }

    override fun save(obj: RetipyEvaluation): RetipyEvaluation
    {
        val savedBean = retipyEvaluationRepository.save(
            RetipyEvaluation.toPersistence(obj))
        return RetipyEvaluation.fromPersistence(savedBean)
    }

    override fun delete(obj: RetipyEvaluation)
    {
        retipyEvaluationRepository.delete(RetipyEvaluation.toPersistence(obj))
    }

    override fun delete(id: Long)
    {
        val evaluation = get(id)
        if (evaluation.status != RetipyEvaluationStatus.Running
            && evaluation.status != RetipyEvaluationStatus.Pending)
        {
            retipyEvaluationRepository.deleteById(id)
        }
        else
        {
            throw IncorrectInputException("Can only delete evaluation with Complete or Error")
        }
    }

    override fun findByDiagnostic(diagnosticId: Long): List<RetipyEvaluation> =
        retipyEvaluationRepository
            .findByDiagnosticId(diagnosticId).map { RetipyEvaluation.fromPersistence(it) }

    override fun getEvaluationsByStatus(status: RetipyEvaluationStatus): List<RetipyEvaluation> =
        retipyEvaluationRepository
            .findByStatus(status)
            .map { RetipyEvaluation.fromPersistence(it) }

    override fun getPendingEvaluations(): List<RetipyEvaluation> =
        getEvaluationsByStatus(RetipyEvaluationStatus.Pending)

    override fun fromDiagnostic(diagnosticId: Long, task: RetipyTask): RetipyEvaluation
    {
        if (task == RetipyTask.None)
            throw IncorrectInputException("You cannot create a new RetipyEvaluation with None name")
        val diagnostic = diagnosticService.get(diagnosticId)
        val existingEvaluations =
            retipyEvaluationRepository.findByDiagnosticIdAndName(diagnosticId, task.name)
        if (existingEvaluations.isNotEmpty()
            && existingEvaluations.filterNot { it.status == RetipyEvaluationStatus.Error }.isNotEmpty())
        {
            throw IncorrectInputException(
                "A task with $task type cannot be created for this diagnostic")
        }
        val image: String =
            if (task == RetipyTask.LandmarksClassification
                || task == RetipyTask.TortuosityFractal
                || task == RetipyTask.TortuosityDensity)
            {
                val segmentation = retipyEvaluationRepository.findByDiagnosticIdAndName(
                    diagnosticId, RetipyTask.Segmentation.name)
                if (segmentation.isEmpty()
                    || segmentation.first().status != RetipyEvaluationStatus.Complete)
                {
                    throw IncorrectInputException(
                        "A segmentation must exist to add a new $task for this diagnostic")
                }
                segmentation.first().image
            }
            else {
                diagnostic.image
            }
        val retipyEvaluation =
            RetipyEvaluation(diagnosticId = diagnosticId, name = task, image = image)
        return save(retipyEvaluation)
    }

    override fun deleteByDiagnostic(diagnosticId: Long)
    {
        retipyEvaluationRepository.deleteByDiagnosticId(diagnosticId)
    }
}
