package co.avaldes.retipy.rest

import co.avaldes.retipy.domain.EvaluationStatus
import co.avaldes.retipy.domain.RetinalEvaluation
import co.avaldes.retipy.domain.TortuosityService
import co.avaldes.retipy.domain.repository.RetinalEvaluationRepository
import co.avaldes.retipy.rest.dto.RetinalEvaluationDTO
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

@CrossOrigin
@RestController
class RetinalEvaluationEndpoint(
        private val evaluationRepository: RetinalEvaluationRepository,
        private val tortuosityService: TortuosityService)
{
    @GetMapping("/retipy/evaluation/{id}")
    fun getEvaluation(@PathVariable id: Long): RetinalEvaluationDTO
    {
        val evaluation = evaluationRepository.findById(id)
        if (evaluation.isPresent)
            return RetinalEvaluationDTO.fromDomain(evaluation.get())
        throw NotFoundException("$id is not a valid evaluation")
    }

    @PutMapping("/retipy/evaluation")
    fun evaluateImage(@RequestBody image: String): Any
    {
        var requestedEvaluation = RetinalEvaluation(0, "", Date(), "[]", image, EvaluationStatus.PENDING)
        requestedEvaluation = evaluationRepository.save(requestedEvaluation)
        val processedEvaluation = tortuosityService.getDensity(requestedEvaluation)

        if (processedEvaluation.status == EvaluationStatus.ERROR)
        {
            evaluationRepository.delete(processedEvaluation)
            throw BadRequestException("Given image cannot be processed")
        }
        evaluationRepository.save(processedEvaluation)
        return RetinalEvaluationDTO.fromDomain(processedEvaluation)
    }

    @PostMapping("/retipy/evaluation")
    fun saveEvaluation(@Valid @RequestBody evaluationDTO: RetinalEvaluationDTO): RetinalEvaluationDTO
    {
        evaluationDTO.id = null
        val evaluation = evaluationRepository.save(RetinalEvaluationDTO.toDomain(evaluationDTO))
        return RetinalEvaluationDTO.fromDomain(evaluation)
    }
}