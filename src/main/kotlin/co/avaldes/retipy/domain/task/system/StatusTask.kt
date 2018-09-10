package co.avaldes.retipy.domain.task.system

import co.avaldes.retipy.domain.task.AbstractRESTTask
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.client.WebClientException

/**
 * Task to verify if the retipy server is running.
 */
class StatusTask(
    retipyUri: String
) : AbstractRESTTask<Boolean>("StatusTask", "/status", retipyUri, emptyMap())
{
    override fun execute(): Boolean
    {
        return try
            {
                getRequest(HttpMethod.GET).retrieve()
                true
            }
            catch(webClientException: WebClientException)
            {
                false
            }
    }
}
