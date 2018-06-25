/*
 * Copyright (C) 2018 - Alejandro Valdes
 *
 * This file is part of retipy.
 *
 * retipy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * retipy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with retipy.  If not, see <http://www.gnu.org/licenses/>.
 */

package co.avaldes.retipy.rest

import co.avaldes.retipy.domain.diagnostic.Diagnostic
import co.avaldes.retipy.domain.diagnostic.DiagnosticStatus
import co.avaldes.retipy.domain.diagnostic.IDiagnosticService
import co.avaldes.retipy.rest.common.BadRequestException
import co.avaldes.retipy.rest.common.NotFoundException
import co.avaldes.retipy.rest.dto.DiagnosticDTO
import co.avaldes.retipy.util.JsonBlob
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
internal class DiagnosticEndpoint(private val diagnosticService: IDiagnosticService)
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
            DiagnosticStatus.COMPLETED,
            Date(),
            Date())
    }

    @PostMapping("/retipy/diagnostic")
    fun addDiagnostic(@RequestBody diagnosticDTO: DiagnosticDTO): DiagnosticDTO
    {
        val id = diagnosticDTO.id ?: 0
        val currentDiagnostic = diagnosticService.findById(id)
        val diagnosticToPersist : Diagnostic =
            if (currentDiagnostic.isPresent)
            {
                val d = currentDiagnostic.get()
                Diagnostic(
                    id,
                    diagnosticDTO.image ?: d.image,
                    diagnosticDTO.diagnostic ?: d.diagnostic,
                    diagnosticDTO.rois?.blob ?: d.rois,
                    DiagnosticStatus.UPDATED,
                    diagnosticDTO.creationDate ?: d.creationDate,
                    diagnosticDTO.updateDate ?: d.updateDate)
            }
            else
            {
                diagnosticDTO.id = id
                DiagnosticDTO.toDomain(diagnosticDTO)
            }
        val savedDiagnostic = diagnosticService.save(diagnosticToPersist)

        return DiagnosticDTO.fromDomain(savedDiagnostic)
    }

    @PostMapping("/retipy/diagnostic/image")
    fun addDiagnosticByImage(@RequestBody image: String): DiagnosticDTO
    {
        if (image.isBlank())
        {
            throw BadRequestException("body must contain a base64 image")
        }
        val diagnostic = Diagnostic(
            0, image, "", "[]",DiagnosticStatus.CREATED, Date(), Date())
        val storedDiagnostic = diagnosticService.save(diagnostic)
        return DiagnosticDTO.fromDomain(storedDiagnostic)
    }

    @DeleteMapping("/retipy/diagnostic/{id}")
    fun deleteDiagnostic(@PathVariable id: Long)
    {
        diagnosticService.deleteById(id)
    }
}
