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

package co.avaldes.retipy.domain.evaluation

import co.avaldes.retipy.domain.Results
import co.avaldes.retipy.domain.tortuosity.ITortuosityService
import co.avaldes.retipy.persistence.repository.IRetinalEvaluationRepository
import org.springframework.stereotype.Service
import java.util.*

/**
 * Domain operations for [RetinalEvaluation].
 *
 * This class implements the interface [IRetinalEvaluationService] which exposes the API.
 */
@Service
internal class RetinalEvaluationService(
    val retinalEvaluationRepository: IRetinalEvaluationRepository,
    val tortuosityService: ITortuosityService)
    : IRetinalEvaluationService
{
    override fun findById(id: Long): RetinalEvaluation?
    {
        val bean = retinalEvaluationRepository.findById(id)
        var retinalEvaluation: RetinalEvaluation? = null
        if (bean.isPresent)
            retinalEvaluation = RetinalEvaluation.fromPersistence(bean.get())
        return retinalEvaluation
    }

    override fun save(retinalEvaluation: RetinalEvaluation): RetinalEvaluation
    {
        val savedBean = retinalEvaluationRepository.save(
            RetinalEvaluation.toPersistence(retinalEvaluation))
        return RetinalEvaluation.fromPersistence(savedBean)
    }

    override fun delete(retinalEvaluation: RetinalEvaluation)
    {
        retinalEvaluationRepository.delete(RetinalEvaluation.toPersistence(retinalEvaluation))
    }

    override fun processImage(image: String, algorithm: String): RetinalEvaluation?
    {
        val result = Results.Result(Results.ORIGINAL, "[]", image)
        var requestedEvaluation = RetinalEvaluation(
            0, "", Date(), Results(listOf(result)), RetinalEvaluation.EvaluationStatus.PENDING)
        requestedEvaluation = save(requestedEvaluation)

        var processedEvaluation: RetinalEvaluation? =
                when(algorithm)
                {
                    "density" -> tortuosityService.getDensity(requestedEvaluation)
                    "fractal" -> tortuosityService.getFractal(requestedEvaluation)
                    else -> null
                }
        if (processedEvaluation == null || processedEvaluation.status == RetinalEvaluation.EvaluationStatus.ERROR)
        {
            delete(requestedEvaluation)
        }
        else
        {
            processedEvaluation = save(processedEvaluation)
        }

        return processedEvaluation
    }
}
