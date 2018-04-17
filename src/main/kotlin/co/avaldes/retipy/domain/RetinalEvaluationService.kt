package co.avaldes.retipy.domain

import co.avaldes.retipy.persistence.repository.RetinalEvaluationRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RetinalEvaluationService(val retinalEvaluationRepository: RetinalEvaluationRepository,
                               val tortuosityService: TortuosityService)
{
    fun findById(id: Long): RetinalEvaluation?
    {
        val bean = retinalEvaluationRepository.findById(id)
        var retinalEvaluation: RetinalEvaluation? = null
        if (bean.isPresent)
            retinalEvaluation = RetinalEvaluation.fromPersistence(bean.get())
        return retinalEvaluation
    }

    fun save(retinalEvaluation: RetinalEvaluation): RetinalEvaluation
    {
        val savedBean = retinalEvaluationRepository.save(
                RetinalEvaluation.toPersistence(retinalEvaluation))
        return RetinalEvaluation.fromPersistence(savedBean)
    }

    fun delete(retinalEvaluation: RetinalEvaluation)
    {
        retinalEvaluationRepository.delete(RetinalEvaluation.toPersistence(retinalEvaluation))
    }

    fun processImage(image: String, algorithm: String): RetinalEvaluation?
    {
        val result = Results.Result(Results.ORIGINAL, "[]", image)
        var requestedEvaluation = RetinalEvaluation(
                0, "", Date(), Results(listOf(result)), RetinalEvaluation.EvaluationStatus.PENDING)
        requestedEvaluation = save(requestedEvaluation)

        var processedEvaluation:RetinalEvaluation? =
                when(algorithm)
                {
                    "density" -> tortuosityService.getDensity(requestedEvaluation)
                    "fractal" -> tortuosityService.getFractal(requestedEvaluation)
                    else -> null
                }
        if (processedEvaluation == null || processedEvaluation.status == RetinalEvaluation.EvaluationStatus.ERROR)
        {
            delete(requestedEvaluation)
        }
        else
        {
            processedEvaluation = save(processedEvaluation)
        }

        return processedEvaluation
    }
}