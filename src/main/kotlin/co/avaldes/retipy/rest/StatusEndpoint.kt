package co.avaldes.retipy.rest

import co.avaldes.retipy.domain.IStatusService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
internal class StatusEndpoint(private val statusService: IStatusService)
{
    @GetMapping("/retipy/status")
    fun status(): ResponseEntity<kotlin.Any> = ResponseEntity(HttpStatus.OK)

    @GetMapping("/retipy/status/backend")
    fun backendStatus(): ResponseEntity<kotlin.Any> = if (statusService.backendStatus())
        {
            ResponseEntity(HttpStatus.OK)
        }
        else
        {
            ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE)
        }
}
