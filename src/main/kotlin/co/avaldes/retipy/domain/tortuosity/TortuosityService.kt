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

package co.avaldes.retipy.domain.tortuosity

import co.avaldes.retipy.domain.common.roi.Roi
import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Service class that provides tortuosity related services
 *
 * This class implements [ITortuosityService] which exposes the API.
 */
@Service
internal class TortuosityService(
    @Value("\${retipy.python.backend.url}") private val retipyUrl: String
) : ITortuosityService {
    private final val endpoint: String = retipyUrl.trim() + "tortuosity/"

    private data class TortuosityRequest(val image: String)
    data class Density(val uri: String, val data: List<Roi>)

    override fun getDensity(evaluation: RetipyEvaluation): RetipyEvaluation {
        TODO("this class will be removed, refactored into a name")
//        val inputImage = evaluation.results.getResult("original")!!.image
//        val template = RestTemplate()
//        val density: Density? = template.postForObject(
//                uri + "density", TortuosityRequest(inputImage), TortuosityRequest::class)
//        if (density != null)
//        {
//            evaluation.results.addResult(Results.Result(density.uri, density.data.blob, inputImage))
//            evaluation.status = RetipyEvaluation.EvaluationStatus.COMPLETE
//        }
//        else
//        {
//            evaluation.status = RetipyEvaluation.EvaluationStatus.ERROR
//        }
//        return evaluation
    }

    override fun getFractal(evaluation: RetipyEvaluation): RetipyEvaluation {
        TODO("this class will be removed, refactored into a name")
//        val inputImage = evaluation.results.getResult("original")!!.image
//        val template = RestTemplate()
//        val density: Density? = template.postForObject(
//                uri + "fractal", TortuosityRequest(inputImage), TortuosityRequest::class)
//        if (density != null)
//        {
//            evaluation.results.addResult(Results.Result(density.uri, density.data.blob, inputImage))
//            evaluation.status = RetipyEvaluation.EvaluationStatus.COMPLETE
//        }
//        else
//        {
//            evaluation.status = RetipyEvaluation.EvaluationStatus.ERROR
//        }
//        return evaluation
    }
}
