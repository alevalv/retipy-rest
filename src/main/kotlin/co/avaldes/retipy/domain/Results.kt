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

package co.avaldes.retipy.domain

import com.fasterxml.jackson.annotation.JsonValue
import javax.persistence.Embeddable
import javax.persistence.Lob

class Results(results:List<Result>)
{
    @Embeddable
    data class Result(val name: String, @Lob val data: String, @Lob val image: String)

    companion object {
        val ORIGINAL = "original"
    }

    private val resultMap = HashMap<String, Result>()

    init
    {
        results.forEach{ result -> resultMap[result.name.toLowerCase()] = result }
    }

    fun addResult(result:Result)
    {
        resultMap[result.name.toLowerCase()] = result
    }

    fun getNames(): List<String>
    {
        return ArrayList(resultMap.keys)
    }

    fun getResult(name: String): Result?
    {
        return resultMap[name.toLowerCase()]
    }

    @JsonValue
    fun getResults(): List<Result>
    {
        return ArrayList(resultMap.values)
    }
}
