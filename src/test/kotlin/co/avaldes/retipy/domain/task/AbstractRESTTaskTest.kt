package co.avaldes.retipy.domain.task

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

const val serverUri = "http://localhost:14897"
const val endpointUri = "/resource"
val defaultProperties = mapOf("property" to "value")

internal class AbstractRESTTaskTest {
    private class RESTTask : AbstractRESTTask<Boolean>(
        "RESTTask", serverUri, endpointUri, defaultProperties) {
        fun addMissingPropertiesTestAccess(properties: Map<String, String>): Map<String, String> {
            return super.addMissingProperties(properties)
        }

        fun getRequestTestAccess(httpMethod: HttpMethod): WebClient.RequestBodySpec {
            return super.getRequest(httpMethod, MediaType.APPLICATION_JSON, serverUri)
        }

        override fun execute(): Boolean? {
            return true
        }
    }

    val get = RouterFunctions.route(
        RequestPredicates.GET(endpointUri),
        HandlerFunction { ServerResponse.ok().build() })
    private val testClient = WebTestClient.bindToServer().baseUrl(serverUri).build()
    private val restTask = RESTTask()

    @BeforeEach
    fun setUp() {
        testClient.get().uri(endpointUri)
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun getRequest() {
        assertNotNull(
            restTask.getRequestTestAccess(HttpMethod.GET), "a request should have been returned")
    }

    @Test
    fun test_addMissingProperties() {
        val propertiesWithDefault = restTask.addMissingPropertiesTestAccess(mapOf())
        assertEquals(
            defaultProperties, propertiesWithDefault, "default properties does not match")
    }

    @Test
    fun test_addMissingProperties_withValue() {
        val properties = mapOf("property" to "anothervalue")
        val propertiesWithDefault = restTask.addMissingPropertiesTestAccess(properties)
        assertEquals(properties, propertiesWithDefault, "properties does not match")
    }

    @Test
    fun test_getTaskName() {
        assertEquals("RESTTask", restTask.taskName)
    }

    @Test
    fun execute() {
        assertTrue(restTask.execute()!!)
    }
}
