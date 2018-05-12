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

import co.avaldes.retipy.domain.Results
import co.avaldes.retipy.util.JsonBlob

data class ResultDTO(val name: String, val data: JsonBlob, val image: String)
{
    companion object
    {
        fun fromList(resultsDTOList: List<ResultDTO>): Results
        {
            return Results(resultsDTOList.map { resultDTO -> toDomain(resultDTO) })
        }

        fun toList(results: Results): List<ResultDTO>
        {
            return results.getResults().map { result -> fromDomain(result) }
        }

        fun toDomain(resultDTO: ResultDTO) =
                Results.Result(resultDTO.name, resultDTO.data.blob, resultDTO.image)

        fun fromDomain(result: Results.Result) =
                ResultDTO(result.name, JsonBlob(result.data), result.image)
    }
}
