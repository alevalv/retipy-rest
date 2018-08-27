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

import co.avaldes.retipy.common.nm.Education
import co.avaldes.retipy.common.nm.Sex
import java.util.*


data class PatientDTO(
    val id: Long,
    val identity: Long,
    val name: String,
    val birthDate: Date,
    val sex: Sex,
    val origin: String,
    val procedence: String,
    val education: Education,
    val race: String,
    val pathologicalPast: List<String>,
    val familiarPast: List<String>,
    val medicines: List<String>,
    val opticalEvaluations: List<OpticalEvaluationDTO>
)
