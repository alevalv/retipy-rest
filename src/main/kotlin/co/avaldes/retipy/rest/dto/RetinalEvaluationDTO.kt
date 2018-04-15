package co.avaldes.retipy.rest.dto

import co.avaldes.retipy.domain.RetinalEvaluation
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import java.util.Date


@JsonRootName("evaluation")
data class RetinalEvaluationDTO(
        var id: Long?,
        val uri: String,
        val timestamp: Date?,
        @JsonIgnore
        var status: RetinalEvaluation.EvaluationStatus?,
        val results: List<ResultDTO>)
{
    companion object
    {
        fun fromDomain(retinalEvaluation: RetinalEvaluation) = RetinalEvaluationDTO(
                id = retinalEvaluation.id,
                uri = retinalEvaluation.uri,
                timestamp = retinalEvaluation.timestamp,
                status = retinalEvaluation.status,
                results = ResultDTO.toList(retinalEvaluation.results))

        fun toDomain(retinalEvaluationDTO: RetinalEvaluationDTO) = RetinalEvaluation(
                id = retinalEvaluationDTO.id ?: 0,
                uri = retinalEvaluationDTO.uri,
                timestamp = retinalEvaluationDTO.timestamp ?: Date(),
                status = retinalEvaluationDTO.status ?: RetinalEvaluation.EvaluationStatus.PENDING,
                results = ResultDTO.fromList(retinalEvaluationDTO.results))
    }
}