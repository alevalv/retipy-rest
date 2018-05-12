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

package co.avaldes.retipy.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer

/**
 * Simple container to avoid deserialization of json data.
 * @property blob the data of the json property.
 */
@JsonDeserialize(using = RoiDeserializer::class)
@JsonSerialize(using = RoiSerializer::class)
data class JsonBlob(val blob: String)

private class RoiDeserializer : StdDeserializer<JsonBlob>(JsonBlob::class.java)
{
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): JsonBlob
    {
        return JsonBlob(p?.codec!!.readTree<TreeNode>(p).toString())
    }
}

private class RoiSerializer : StdSerializer<JsonBlob>(JsonBlob::class.java)
{
    override fun serialize(value: JsonBlob?, gen: JsonGenerator?, provider: SerializerProvider?)
    {
        if (value == null)
            gen?.writeNull()
        else
            gen?.writeRawValue(value.blob)
    }
}
