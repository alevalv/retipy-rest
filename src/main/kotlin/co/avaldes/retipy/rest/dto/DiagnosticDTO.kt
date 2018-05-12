package co.avaldes.retipy.rest.dto

import co.avaldes.retipy.domain.diagnostic.Diagnostic
import co.avaldes.retipy.domain.diagnostic.DiagnosticStatus
import co.avaldes.retipy.util.JsonBlob
import java.util.*

data class DiagnosticDTO(
        var id: Long?,
        val image: String,
        val diagnostic: String,
        val rois:JsonBlob,
        val notes:String,
        val status: DiagnosticStatus,
        val creationDate: Date?,
        val updateDate: Date?)
{
    companion object
    {
        fun fromDomain(diagnostic: Diagnostic) = DiagnosticDTO(
            diagnostic.id,
            diagnostic.image,
            diagnostic.diagnostic,
            JsonBlob(diagnostic.rois),
            diagnostic.notes,
            diagnostic.status,
            diagnostic.creationDate,
            diagnostic.updateDate)

        fun toDomain(diagnosticDTO: DiagnosticDTO) = Diagnostic(
            diagnosticDTO.id ?: 0,
            diagnosticDTO.image,
            diagnosticDTO.diagnostic,
            diagnosticDTO.rois.blob,
            diagnosticDTO.notes)
    }
}
