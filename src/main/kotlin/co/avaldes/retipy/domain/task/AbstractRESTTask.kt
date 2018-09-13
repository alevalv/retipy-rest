package co.avaldes.retipy.domain.task

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

abstract class AbstractRESTTask<R>(
    val taskName: String,
    val uri: String,
    val retipyUri: String,
    val defaultConfiguration: Map<String, String>
): ITask<R>
{
    protected val webClient: WebClient =  WebClient.builder()
            .baseUrl(retipyUri)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultUriVariables(Collections.singletonMap("url", retipyUri))
            .build()

    protected fun addMissingProperties(properties: Map<String, String>): Map<String, String>
    {
        val mutableMap: MutableMap<String, String> = HashMap(properties)
        defaultConfiguration.filterNot { properties.containsKey(it.key) }.forEach { key, value ->
            mutableMap[key] = value
        }
        return mutableMap
    }

    protected fun getRequest(
        method: HttpMethod,
        mediaType: MediaType = MediaType.APPLICATION_JSON): WebClient.RequestBodySpec =
        webClient.method(method).uri(uri).accept(mediaType)

    abstract override fun execute(): R?
}