package co.avaldes.retipy.domain

import co.avaldes.retipy.util.JsonBlob
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import java.util.*

@Service
class TortuosityService(@Value("\${retipy.python.backend.url}") private val retipyUrl: String) {
    private final val endpoint: String = retipyUrl.trim() + "tortuosity/"

    private data class TortuosityRequest(val image: String)
    data class Density(val uri: String, val data: JsonBlob)

    fun getDensity(evaluation: RetinalEvaluation): RetinalEvaluation
    {
        val template = RestTemplate()
        val density: Density? = template.postForObject(
                endpoint + "density", TortuosityRequest(evaluation.image), TortuosityRequest::class)
        if (density != null)
        {
            evaluation.uri = density.uri
            evaluation.data = density.data.blob
            evaluation.status = EvaluationStatus.COMPLETE
        }
        else
        {
            evaluation.status = EvaluationStatus.ERROR
        }
        return evaluation
    }
}
