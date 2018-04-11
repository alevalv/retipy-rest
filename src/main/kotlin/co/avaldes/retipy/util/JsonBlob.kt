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
