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