package co.avaldes.retipy.rest

import co.avaldes.retipy.domain.repository.RetinalEvaluationRepository
import co.avaldes.retipy.rest.dto.RetinalEvaluationDTO
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@CrossOrigin
@RestController
class RetinalEvaluationEndpoint(
        private val evaluationRepository: RetinalEvaluationRepository)
{
    @GetMapping("/retipy/evaluation/{id}")
    fun getEvaluation(@PathVariable id: Long): Any
    {
        val evaluation = evaluationRepository.findById(id)
        if (evaluation.isPresent)
            return RetinalEvaluationDTO.fromDomain(evaluation.get())
        throw NotFoundException("$id is not a valid evaluation")
    }

    @PostMapping("/retipy/evaluation")
    fun saveEvaluation(@Valid @RequestBody evaluationDTO: RetinalEvaluationDTO): Any
    {
        evaluationDTO.id = null
        val evaluation = evaluationRepository.save(RetinalEvaluationDTO.toDomain(evaluationDTO))
        return RetinalEvaluationDTO.fromDomain(evaluation)
    }
}