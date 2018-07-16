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

package co.avaldes.retipy.rest.dto.record

import co.avaldes.retipy.domain.record.Record
import java.util.*

data class RecordDTO(
    val id: Long,
    val version: Long,
    val creationDate: Date,
    val updateDate: Date,
    val visualLeftEye: String,
    val visualRightEye: String,
    val visualLeftPh: String,
    val visualRightPh: String,
    val pupilLeftEyeRD: Int,
    val pupilLeftEyeRC: Int,
    val pupilLeftEyeDPA: Int,
    val pupilRightEyeRD: Int,
    val pupilRightEyeRC: Int,
    val pupilRightEyeDPA: Int,
    val biomicroscopy: String,
    val PIO: String,
    val evaluationId: Long
)
{
    companion object
    {
        fun fromDomain(record: Record) = RecordDTO(
            record.id,
            record.version,
            record.creationDate,
            record.updateDate,
            record.visualLeftEye,
            record.visualRightEye,
            record.visualLeftPh,
            record.visualRightPh,
            record.pupilLeftEyeRD,
            record.pupilLeftEyeRC,
            record.pupilLeftEyeDPA,
            record.pupilRightEyeRD,
            record.pupilRightEyeRC,
            record.pupilRightEyeDPA,
            record.biomicroscopy,
            record.PIO,
            record.evaluationId
        )

        fun toDomain(recordDTO: RecordDTO) = Record(
            recordDTO.id,
            recordDTO.version,
            recordDTO.creationDate,
            recordDTO.updateDate,
            recordDTO.visualLeftEye,
            recordDTO.visualRightEye,
            recordDTO.visualLeftPh,
            recordDTO.visualRightPh,
            recordDTO.pupilLeftEyeRD,
            recordDTO.pupilLeftEyeRC,
            recordDTO.pupilLeftEyeDPA,
            recordDTO.pupilRightEyeRD,
            recordDTO.pupilRightEyeRC,
            recordDTO.pupilRightEyeDPA,
            recordDTO.biomicroscopy,
            recordDTO.PIO,
            recordDTO.evaluationId
        )
    }
}
