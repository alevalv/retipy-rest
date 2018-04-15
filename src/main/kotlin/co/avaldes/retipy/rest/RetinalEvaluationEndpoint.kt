package co.avaldes.retipy.rest

import co.avaldes.retipy.domain.Results
import co.avaldes.retipy.domain.RetinalEvaluation
import co.avaldes.retipy.domain.RetinalEvaluationService
import co.avaldes.retipy.domain.TortuosityService
import co.avaldes.retipy.persistence.repository.RetinalEvaluationRepository
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
        private val retinalEvaluationService: RetinalEvaluationService)
{
    @GetMapping("/retipy/evaluation/{id}")
    fun getEvaluation(@PathVariable id: Long): RetinalEvaluationDTO
    {
        val evaluation = retinalEvaluationService.findById(id)
        if (evaluation != null)
            return RetinalEvaluationDTO.fromDomain(evaluation)
        throw NotFoundException("$id is not a valid evaluation")
    }

    @PutMapping("/retipy/evaluation")
    fun evaluateImage(@RequestBody image: String): Any
    {
        val evaluation = retinalEvaluationService.processImage(image)
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