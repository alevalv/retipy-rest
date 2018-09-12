package co.avaldes.retipy.domain.task.system

import co.avaldes.retipy.domain.task.AbstractRESTTask
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType

/**
 * Task to verify if the retipy server is running.
 */
class StatusTask(
    retipyUri: String
) : AbstractRESTTask<Boolean>("StatusTask", "/status", retipyUri, emptyMap())
{
    override fun execute(): Boolean
    {
        var status = false
        try
        {
            getRequest(HttpMethod.GET, MediaType.ALL).exchange().doOnSuccess { status = true }.doOnError { status = false }
        }
        catch(webClientException: Exception)
        {
            status = false
        }
        return status
    }
}
