package co.avaldes.retipy.rest.dto

import co.avaldes.retipy.domain.RetinalEvaluation
import com.fasterxml.jackson.annotation.JsonRootName
import java.util.Date


@JsonRootName("evaluation")
data class RetinalEvaluationDTO(
        var id: Long?,
        val uri: String,
        val timestamp: Date?,
        var data: RoiDTO,
        val image: String)
{
    companion object
    {
        fun fromDomain(retinalEvaluation: RetinalEvaluation) = RetinalEvaluationDTO(
                id = retinalEvaluation.id,
                uri = retinalEvaluation.uri,
                timestamp = retinalEvaluation.timestamp,
                data = RoiDTO(retinalEvaluation.data),
                image = retinalEvaluation.image)

        fun toDomain(retinalEvaluationDTO: RetinalEvaluationDTO) = RetinalEvaluation(
                id = retinalEvaluationDTO.id ?: 0,
                uri = retinalEvaluationDTO.uri,
                timestamp = retinalEvaluationDTO.timestamp ?: Date(),
                data = retinalEvaluationDTO.data.data,
                image = retinalEvaluationDTO.image)
    }
}