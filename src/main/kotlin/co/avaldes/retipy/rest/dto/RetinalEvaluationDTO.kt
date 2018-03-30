package co.avaldes.retipy.rest.dto

import co.avaldes.retipy.domain.RetinalEvaluation
import com.fasterxml.jackson.annotation.JsonRootName


@JsonRootName("evaluation")
data class RetinalEvaluationDTO(
        var id: Long?,
        var resource_url: String,
        var data: String) {
    companion object {
        fun fromDomain(retinalEvaluation: RetinalEvaluation) = RetinalEvaluationDTO(
                id = retinalEvaluation.id,
                resource_url = retinalEvaluation.resource_url,
                data = retinalEvaluation.data)

        fun toDomain(retinalEvaluationDTO: RetinalEvaluationDTO) = RetinalEvaluation(
                id = retinalEvaluationDTO.id,
                resource_url = retinalEvaluationDTO.resource_url,
                data = retinalEvaluationDTO.data)
    }
}