package co.avaldes.retipy.rest.dto

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonDeserialize(using = RoiDeserializer::class)
@JsonSerialize(using = RoiSerializer::class)
data class RoiDTO(
        val data: String)

private class RoiDeserializer : StdDeserializer<RoiDTO>(RoiDTO::class.java)
{
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): RoiDTO
    {
        return RoiDTO(p?.codec!!.readTree<TreeNode>(p).toString())
    }
}

private class RoiSerializer : StdSerializer<RoiDTO>(RoiDTO::class.java)
{
    override fun serialize(value: RoiDTO?, gen: JsonGenerator?, provider: SerializerProvider?)
    {
        if (value == null)
            gen?.writeNull()
        else
            gen?.writeRawValue(value.data)
    }
}
