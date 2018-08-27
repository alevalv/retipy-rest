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

package co.avaldes.retipy.domain.diagnostic

import co.avaldes.retipy.persistence.diagnostic.DiagnosticBean
import co.avaldes.retipy.persistence.diagnostic.DiagnosticStatus
import co.avaldes.retipy.rest.common.IncorrectInputException
import java.util.*

data class Diagnostic(
    var id: Long = 0,
    var image: String? = "",
    var diagnostic: String = "",
    var rois: List<Roi> = emptyList(),
    var status: DiagnosticStatus = DiagnosticStatus.CREATED,
    var creationDate: Date = Date(),
    var updateDate: Date = Date())
{
    companion object
    {
        fun fromPersistence(diagnosticBean: DiagnosticBean) = Diagnostic(
            diagnosticBean.id,
            diagnosticBean.image,
            diagnosticBean.diagnostic,
            Roi.fromPersistence(diagnosticBean.rois),
            diagnosticBean.status,
            diagnosticBean.creationDate,
            diagnosticBean.updateDate
        )

        fun toPersistence(diagnostic: Diagnostic) = DiagnosticBean(
            diagnostic.id,
            diagnostic.image ?: throw IncorrectInputException("A diagnostic cannot have a null image"),
            diagnostic.diagnostic,
            Roi.toPersistence(diagnostic.rois),
            diagnostic.status,
            diagnostic.creationDate,
            diagnostic.updateDate
        )
    }
}
