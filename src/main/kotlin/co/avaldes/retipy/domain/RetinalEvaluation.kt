package co.avaldes.retipy.domain

import co.avaldes.retipy.persistence.RetinalEvaluationBean
import java.util.*
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.PrePersist
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

data class RetinalEvaluation(
        val id: Long,
        var uri: String,
        var timestamp: Date,
        val results: Results,
        var status: EvaluationStatus)
{
    enum class EvaluationStatus
    {
        PENDING, COMPLETE, ERROR
    }

    override fun toString(): String = "Evaluation($id, $uri)"

    companion object
    {
        fun toPersistence(retinalEvaluation: RetinalEvaluation) = RetinalEvaluationBean(
                retinalEvaluation.id,
                retinalEvaluation.uri,
                retinalEvaluation.timestamp,
                retinalEvaluation.results.getResults(),
                retinalEvaluation.status)

        fun fromPersistence(retinalEvaluationBean: RetinalEvaluationBean) = RetinalEvaluation(
                retinalEvaluationBean.id,
                retinalEvaluationBean.uri,
                retinalEvaluationBean.timestamp,
                Results(retinalEvaluationBean.results),
                retinalEvaluationBean.status)
    }
}
