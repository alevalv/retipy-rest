package co.avaldes.retipy.domain.task.tortuosity

import co.avaldes.retipy.domain.common.roi.Roi
import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation
import co.avaldes.retipy.domain.task.AbstractRESTTask
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.BodyInserters

/**
 * Abstract class that access the retipy backend and obtains a tortuosity measure given a segmented
 * retinal image with only vessel information.
 */
abstract class AbstractTortuosityTask(
    retipyUri: String,
    uri: String,
    private val retipyEvaluation: RetipyEvaluation
    // private val inputProperties: Map<String, String> = emptyMap() TODO
) : AbstractRESTTask<RetipyEvaluation>(
    "TortuosityDensityRESTTask",
    uri,
    retipyUri,
    mapOf("window_size" to "56",
        "min_pixels" to "10",
        "creation_method" to "separated",
        "threshold" to "0.97")
) {
    private data class TortuosityDensityRequest(val image: String)
    private data class TortuosityDensityResponse(val uri: String, val data: List<Roi>)

    private val logger: Logger = LoggerFactory.getLogger(TortuosityDensityTask::class.java)

    override fun execute(): RetipyEvaluation {
        logger.info("Starting Task")
        val requestWithBody = getRequest(HttpMethod.POST)
            .body(BodyInserters.fromObject(
                TortuosityDensityRequest(
                    retipyEvaluation.image)))

        try {
            val response = requestWithBody.retrieve()
                .bodyToMono(TortuosityDensityResponse::class.java)
                .block()!!
            retipyEvaluation.rois = response.data
            retipyEvaluation.rois.forEach { it.color = "white" }
            retipyEvaluation.status = RetipyEvaluationStatus.Complete
            logger.info("Completed")
        } catch (webClientException: Exception) { // TODO make this exception more specific
            retipyEvaluation.status = RetipyEvaluationStatus.Error
            logger.info("Failed $webClientException")
        }
        return retipyEvaluation
    }
}
