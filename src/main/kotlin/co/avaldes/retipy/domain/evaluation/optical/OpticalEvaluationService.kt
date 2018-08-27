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

package co.avaldes.retipy.domain.evaluation.optical

import co.avaldes.retipy.persistence.evaluation.optical.IOpticalEvaluationRepository
import co.avaldes.retipy.rest.common.IncorrectInputException
import org.springframework.stereotype.Service

/**
 * Service for retrieving [OpticalEvaluation].
 */
@Service
internal class OpticalEvaluationService(private var opticalEvaluationRepository: IOpticalEvaluationRepository)
    : IOpticalEvaluationService
{
    override fun find(id: Long): OpticalEvaluation?
    {
        var opticalEvaluation: OpticalEvaluation? = null
        val persistedBean = opticalEvaluationRepository.findById(id)
        if (persistedBean.isPresent)
        {
            opticalEvaluation = OpticalEvaluation.fromPersistence(persistedBean.get())
        }
        return opticalEvaluation
    }

    override fun get(id: Long): OpticalEvaluation
    {
        return find(id) ?: throw IncorrectInputException("Optical Evaluation with id $id not found")
    }
}
