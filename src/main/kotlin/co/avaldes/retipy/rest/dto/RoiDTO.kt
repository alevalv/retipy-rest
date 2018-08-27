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

import co.avaldes.retipy.domain.diagnostic.Roi

data class RoiDTO (val roi_x: List<Int>, val roi_y: List<Int>, val notes: String)
{
    companion object
    {
        fun fromDomain(roi: Roi) = RoiDTO(roi.roi_x, roi.roi_y, roi.notes)

        fun toDomain(roiDTO: RoiDTO) = Roi(roiDTO.roi_x, roiDTO.roi_y, roiDTO.notes)
    }
}
