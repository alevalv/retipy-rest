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

import co.avaldes.retipy.persistence.evaluation.retinal.IRetinalEvaluationRepository
import co.avaldes.retipy.rest.common.IncorrectInputException
import org.springframework.stereotype.Service

/**
 * Domain operations for [RetipyEvaluation].
 *
 * This class implements the interface [IRetipyEvaluationService] which exposes the API.
 */
@Service
internal class RetipyEvaluationService(
    private val retinalEvaluationRepository: IRetinalEvaluationRepository)
    : IRetipyEvaluationService
{
    override fun find(id: Long): RetipyEvaluation?
    {
        val bean = retinalEvaluationRepository.findById(id)
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
        val savedBean = retinalEvaluationRepository.save(
            RetipyEvaluation.toPersistence(obj))
        return RetipyEvaluation.fromPersistence(savedBean)
    }

    override fun delete(obj: RetipyEvaluation)
    {
        retinalEvaluationRepository.delete(RetipyEvaluation.toPersistence(obj))
    }

    override fun delete(id: Long)
    {
        retinalEvaluationRepository.deleteById(id)
    }

    override fun processImage(image: String, algorithm: String): RetipyEvaluation?
    {
        TODO("Need to write the task backend before tackling this method")
    }
}
