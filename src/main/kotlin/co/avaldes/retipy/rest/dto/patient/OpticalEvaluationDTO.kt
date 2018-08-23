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

package co.avaldes.retipy.rest.dto.patient

import co.avaldes.retipy.domain.evaluation.optical.OpticalEvaluation
import java.util.*

data class OpticalEvaluationDTO(
    val id: Long,
    val version: Long,
    val creationDate: Date?,
    val updateDate: Date?,
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
    val biomicroscopy: Map<String, String>,
    val intraocularPressure: String,
    val evaluationId: Long
)
{
    companion object
    {
        fun fromDomain(opticalEvaluation: OpticalEvaluation) = OpticalEvaluationDTO(
            opticalEvaluation.id,
            opticalEvaluation.version,
            opticalEvaluation.creationDate,
            opticalEvaluation.updateDate,
            opticalEvaluation.visualLeftEye,
            opticalEvaluation.visualRightEye,
            opticalEvaluation.visualLeftPh,
            opticalEvaluation.visualRightPh,
            opticalEvaluation.pupilLeftEyeRD,
            opticalEvaluation.pupilLeftEyeRC,
            opticalEvaluation.pupilLeftEyeDPA,
            opticalEvaluation.pupilRightEyeRD,
            opticalEvaluation.pupilRightEyeRC,
            opticalEvaluation.pupilRightEyeDPA,
            opticalEvaluation.biomicroscopy,
            opticalEvaluation.ocularIntraPressure,
            opticalEvaluation.evaluationId
        )

        fun toDomain(opticalEvaluationDTO: OpticalEvaluationDTO) = OpticalEvaluation(
            opticalEvaluationDTO.id,
            opticalEvaluationDTO.version,
            opticalEvaluationDTO.creationDate ?: Date(),
            opticalEvaluationDTO.updateDate ?: Date(),
            opticalEvaluationDTO.visualLeftEye,
            opticalEvaluationDTO.visualRightEye,
            opticalEvaluationDTO.visualLeftPh,
            opticalEvaluationDTO.visualRightPh,
            opticalEvaluationDTO.pupilLeftEyeRD,
            opticalEvaluationDTO.pupilLeftEyeRC,
            opticalEvaluationDTO.pupilLeftEyeDPA,
            opticalEvaluationDTO.pupilRightEyeRD,
            opticalEvaluationDTO.pupilRightEyeRC,
            opticalEvaluationDTO.pupilRightEyeDPA,
            opticalEvaluationDTO.biomicroscopy.toMutableMap(),
            opticalEvaluationDTO.intraocularPressure,
            opticalEvaluationDTO.evaluationId
        )
    }
}
