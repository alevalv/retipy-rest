package co.avaldes.retipy.rest.dto

import co.avaldes.retipy.domain.EvaluationStatus
import co.avaldes.retipy.domain.RetinalEvaluation
import co.avaldes.retipy.util.JsonBlob
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import java.util.Date


@JsonRootName("evaluation")
data class RetinalEvaluationDTO(
        var id: Long?,
        val uri: String,
        val timestamp: Date?,
        var data: JsonBlob,
        val image: String,
        @JsonIgnore
        val status: EvaluationStatus)
{
    companion object
    {
        fun fromDomain(retinalEvaluation: RetinalEvaluation) = RetinalEvaluationDTO(
                id = retinalEvaluation.id,
                uri = retinalEvaluation.uri,
                timestamp = retinalEvaluation.timestamp,
                data = JsonBlob(retinalEvaluation.data),
                image = retinalEvaluation.image,
                status = retinalEvaluation.status)

        fun toDomain(retinalEvaluationDTO: RetinalEvaluationDTO) = RetinalEvaluation(
                id = retinalEvaluationDTO.id ?: 0,
                uri = retinalEvaluationDTO.uri,
                timestamp = retinalEvaluationDTO.timestamp ?: Date(),
                data = retinalEvaluationDTO.data.blob,
                image = retinalEvaluationDTO.image,
                status = retinalEvaluationDTO.status)
    }
}