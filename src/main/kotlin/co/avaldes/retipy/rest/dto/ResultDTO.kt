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