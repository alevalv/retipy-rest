package co.avaldes.retipy.rest

import co.avaldes.retipy.domain.DiagnosticService
import co.avaldes.retipy.domain.diagnostic.DiagnosticStatus
import co.avaldes.retipy.rest.dto.DiagnosticDTO
import co.avaldes.retipy.util.JsonBlob
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * The REST endpoint that provides all operations related with retinal diagnostics.
 *
 * This endpoint is placed at /retipy/diagnostic and implements all CRUD operations of the
 * [DiagnosticDTO] object.
 */
@CrossOrigin
@RestController
class DiagnosticEndpoint(private val diagnosticService: DiagnosticService)
{
    @GetMapping("/retipy/diagnostic/{id}")
    fun getDiagnostic(@PathVariable id: Long): DiagnosticDTO
    {
        val diagnostic = diagnosticService.findById(id)
        return if (diagnostic.isPresent) DiagnosticDTO.fromDomain(diagnostic.get())
            else throw NotFoundException("$id is not a valid diagnostic")
    }

    @GetMapping("/retipy/diagnostic/example")
    fun exampleDiagnostic(): DiagnosticDTO
    {
        return DiagnosticDTO(
            1,
            "",
            "some diagnostic",
            JsonBlob("[]"),
            "this is an observation",
            DiagnosticStatus.COMPLETED,
            Date(),
            Date())
    }

    @PutMapping("/retipy/diagnostic")
    fun addDiagnostic(diagnosticDTO: DiagnosticDTO): DiagnosticDTO
    {
        diagnosticDTO.id = 0
    }
}
