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

package co.avaldes.retipy.rest

import co.avaldes.retipy.domain.Results
import co.avaldes.retipy.domain.evaluation.IRetinalEvaluationService
import co.avaldes.retipy.domain.evaluation.RetinalEvaluation
import co.avaldes.retipy.rest.common.BadRequestException
import co.avaldes.retipy.rest.common.NotFoundException
import co.avaldes.retipy.rest.dto.ResultDTO
import co.avaldes.retipy.rest.dto.RetinalEvaluationDTO
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * Endpoint that implements a REST CRUD for [RetinalEvaluationDTO].
 *
 * The rest endpoint is defined as [/retipy/evaluation]
 */
@CrossOrigin
@RestController
internal class RetinalEvaluationEndpoint(private val retinalEvaluationService: IRetinalEvaluationService)
{
    @GetMapping("/retipy/evaluation/{id}")
    fun getEvaluation(@PathVariable id: Long): RetinalEvaluationDTO
    {
        val evaluation = retinalEvaluationService.find(id)
            ?: throw  NotFoundException("$id is not a valid evaluation")
        return RetinalEvaluationDTO.fromDomain(evaluation)
    }

    @GetMapping("/retipy/evaluation/{id}/{result}")
    fun getEvaluationResult(@PathVariable id: Long, @PathVariable result: Long): ResultDTO
    {
        val evaluation = retinalEvaluationService.find(id)
            ?: throw  NotFoundException("$id is not a valid evaluation")
        val results = evaluation.results.getResults()
        if (result >= results.size)
            throw NotFoundException("$result is not a valid result id for evaluation $id")
        return ResultDTO.fromDomain(results[result.toInt()])
    }

    @PutMapping("/retipy/evaluation/{id}/result")
    fun putResult(@PathVariable id: Long, @RequestBody resultDTO: ResultDTO): RetinalEvaluationDTO
    {
        var evaluation = retinalEvaluationService.find(id)
            ?: throw  NotFoundException("$id is not a valid evaluation")
        val result: Results.Result =
            if (resultDTO.image.isEmpty())
                Results.Result(
                    resultDTO.name,
                    resultDTO.data.blob,
                    evaluation.results.getResult(Results.ORIGINAL)!!.image)
            else ResultDTO.toDomain(resultDTO)
        evaluation.results.addResult(result)
        evaluation = retinalEvaluationService.save(evaluation)
        return RetinalEvaluationDTO.fromDomain(evaluation)
    }

    @PostMapping("/retipy/evaluation/{algorithm}")
    fun evaluateImage(@RequestBody image: String, @PathVariable algorithm: String): Any
    {
        val algorithmWithDefault = if (algorithm.isBlank()) "density" else algorithm

        val evaluation = retinalEvaluationService.processImage(image, algorithmWithDefault)
            ?: throw BadRequestException("Given image cannot be processed")
        return RetinalEvaluationDTO.fromDomain(evaluation)
    }

    @PostMapping("/retipy/evaluation")
    fun saveEvaluation(@Valid @RequestBody evaluationDTO: RetinalEvaluationDTO): RetinalEvaluationDTO
    {
        evaluationDTO.id = null
        evaluationDTO.status = RetinalEvaluation.EvaluationStatus.COMPLETE
        var evaluation = RetinalEvaluationDTO.toDomain(evaluationDTO)
        evaluation = retinalEvaluationService.save(evaluation)
        return RetinalEvaluationDTO.fromDomain(evaluation)
    }

    @DeleteMapping("/retipy/evaluation/{id}")
    fun deleteEvaluation(@PathVariable id: Long)
    {
        val evaluation = retinalEvaluationService.find(id)
        if (evaluation != null)
        {
            retinalEvaluationService.delete(evaluation)
        }
    }
}
