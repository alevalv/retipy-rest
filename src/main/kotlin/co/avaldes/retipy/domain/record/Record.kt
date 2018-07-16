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

package co.avaldes.retipy.domain.record

import co.avaldes.retipy.persistence.record.MedicalRecordBean
import java.util.*

data class Record(
    var id: Long,
    var version: Long,
    var creationDate: Date,
    var updateDate: Date,
    var visualLeftEye: String,
    var visualRightEye: String,
    var visualLeftPh: String,
    var visualRightPh: String,
    var pupilLeftEyeRD: Int,
    var pupilLeftEyeRC: Int,
    var pupilLeftEyeDPA: Int,
    var pupilRightEyeRD: Int,
    var pupilRightEyeRC: Int,
    var pupilRightEyeDPA: Int,
    var biomicroscopy: String,
    var PIO: String,
    var evaluationId: Long)
{
    companion object
    {
        fun fromPersistence(medicalRecordBean: MedicalRecordBean) = Record(
            medicalRecordBean.id,
            medicalRecordBean.version,
            medicalRecordBean.creationDate,
            medicalRecordBean.updateDate,
            medicalRecordBean.visualLeftEye,
            medicalRecordBean.visualRightEye,
            medicalRecordBean.visualLeftPh,
            medicalRecordBean.visualRightPh,
            medicalRecordBean.pupilLeftEyeRD,
            medicalRecordBean.pupilLeftEyeRC,
            medicalRecordBean.pupilLeftEyeDPA,
            medicalRecordBean.pupilRightEyeRD,
            medicalRecordBean.pupilRightEyeRC,
            medicalRecordBean.pupilRightEyeDPA,
            medicalRecordBean.biomicroscopy,
            medicalRecordBean.PIO,
            medicalRecordBean.evaluationId)

        fun toPersistence(record: Record) = MedicalRecordBean(
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
    }
}

