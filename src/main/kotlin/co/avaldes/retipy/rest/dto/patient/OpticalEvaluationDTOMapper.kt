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

import co.avaldes.retipy.domain.diagnostic.Diagnostic
import co.avaldes.retipy.domain.evaluation.optical.IOpticalEvaluationService
import co.avaldes.retipy.domain.evaluation.optical.OpticalEvaluation
import co.avaldes.retipy.rest.common.IDTOMapper
import org.springframework.stereotype.Component
import java.util.*

/**
 * Mapper class to connect rest layer to domain layer of [OpticalEvaluation].
 */
@Component
class OpticalEvaluationDTOMapper(private val opticalEvaluationService: IOpticalEvaluationService)
    : IDTOMapper<OpticalEvaluation, OpticalEvaluationDTO>
{
    /**
     * Converts the given [OpticalEvaluation] into its DTO counterpart [OpticalEvaluationDTO]
     */
    override fun fromDomain(domainObject: OpticalEvaluation) = OpticalEvaluationDTO(
        domainObject.id,
        domainObject.version,
        domainObject.creationDate,
        domainObject.updateDate,
        domainObject.visualLeftEye,
        domainObject.visualRightEye,
        domainObject.visualLeftPh,
        domainObject.visualRightPh,
        domainObject.pupilLeftEyeRD,
        domainObject.pupilLeftEyeRC,
        domainObject.pupilLeftEyeDPA,
        domainObject.pupilRightEyeRD,
        domainObject.pupilRightEyeRC,
        domainObject.pupilRightEyeDPA,
        domainObject.biomicroscopy,
        domainObject.ocularIntraPressure,
        domainObject.getDiagnostics().map { it.id }
    )

    /**
     * Converts the DTO [OpticalEvaluationDTO] to its domain counterpart [OpticalEvaluation]
     * Will throw exception if the given DTO has an id of an object that does not exist.
     */
    override fun toDomain(dto: OpticalEvaluationDTO): OpticalEvaluation
    {
        var diagnostics: List<Diagnostic> = emptyList()
        if (dto.id != 0L)
        {
            val opticalEvaluation = opticalEvaluationService.get(dto.id)
            diagnostics = opticalEvaluation.getDiagnostics()
        }
        return OpticalEvaluation(
            dto.id,
            dto.version,
            dto.creationDate ?: Date(),
            dto.updateDate ?: Date(),
            dto.visualLeftEye,
            dto.visualRightEye,
            dto.visualLeftPh,
            dto.visualRightPh,
            dto.pupilLeftEyeRD,
            dto.pupilLeftEyeRC,
            dto.pupilLeftEyeDPA,
            dto.pupilRightEyeRD,
            dto.pupilRightEyeRC,
            dto.pupilRightEyeDPA,
            dto.biomicroscopy.toMutableMap(),
            dto.intraocularPressure,
            diagnostics
        )
    }
}
