package co.avaldes.retipy.rest

import co.avaldes.retipy.domain.Results
import co.avaldes.retipy.domain.RetinalEvaluation
import co.avaldes.retipy.domain.RetinalEvaluationService
import co.avaldes.retipy.rest.dto.ResultDTO
import co.avaldes.retipy.rest.dto.RetinalEvaluationDTO
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@CrossOrigin
@RestController
class RetinalEvaluationEndpoint(private val retinalEvaluationService: RetinalEvaluationService)
{
    @GetMapping("/retipy/evaluation/{id}")
    fun getEvaluation(@PathVariable id: Long): RetinalEvaluationDTO
    {
        val evaluation = retinalEvaluationService.findById(id)
                ?: throw  NotFoundException("$id is not a valid evaluation")
        return RetinalEvaluationDTO.fromDomain(evaluation)
    }

    @GetMapping("/retipy/evaluation/{id}/{result}")
    fun getEvaluationResult(@PathVariable id: Long, @PathVariable result: Long): ResultDTO {
        val evaluation = retinalEvaluationService.findById(id)
                ?: throw  NotFoundException("$id is not a valid evaluation")
        val results = evaluation.results.getResults()
        if (result >= results.size)
            throw NotFoundException("$result is not a valid result id for evaluation $id")
        return ResultDTO.fromDomain(results[result.toInt()])
    }

    @PutMapping("/retipy/evaluation/{id}/result")
    fun putResult(@PathVariable id: Long, @RequestBody resultDTO: ResultDTO): RetinalEvaluationDTO
    {
        var evaluation = retinalEvaluationService.findById(id)
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

    @PutMapping("/retipy/evaluation/{algorithm}")
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
}