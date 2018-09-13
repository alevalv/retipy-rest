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

package co.avaldes.retipy.rest.dto

import co.avaldes.retipy.domain.diagnostic.Diagnostic
import co.avaldes.retipy.persistence.diagnostic.DiagnosticStatus
import java.util.*

/**
 * DTO class to transfer a diagnostic with on the rest layer.
 */
data class DiagnosticDTO(
    val id: Long,
    val image: String?,
    val diagnostic: String,
    val rois: List<RoiDTO>,
    val status: DiagnosticStatus?,
    val creationDate: Date?,
    val updateDate: Date?)
{
    companion object
    {
        fun fromDomain(diagnostic: Diagnostic) = DiagnosticDTO(
            diagnostic.id,
            diagnostic.image,
            diagnostic.diagnostic,
            diagnostic.rois.map {RoiDTO.fromDomain(it)},
            diagnostic.status,
            diagnostic.creationDate,
            diagnostic.updateDate)

        fun toDomain(diagnosticDTO: DiagnosticDTO) = Diagnostic(
            diagnosticDTO.id,
            diagnosticDTO.image ?: "",
            diagnosticDTO.diagnostic,
            diagnosticDTO.rois.map { RoiDTO.toDomain(it) },
            diagnosticDTO.status ?: DiagnosticStatus.Created,
            diagnosticDTO.creationDate ?: Date(),
            diagnosticDTO.updateDate ?: Date())
    }
}
