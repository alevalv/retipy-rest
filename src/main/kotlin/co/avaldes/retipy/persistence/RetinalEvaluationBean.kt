package co.avaldes.retipy.persistence

import co.avaldes.retipy.domain.Results
import co.avaldes.retipy.domain.RetinalEvaluation
import java.util.*
import javax.persistence.ElementCollection
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.PrePersist
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(name="RetinalEvaluation")
data class RetinalEvaluationBean(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
        var uri: String,
        @Temporal(TemporalType.TIMESTAMP) var timestamp: Date,
        @ElementCollection val results: List<Results.Result>,
        var status: RetinalEvaluation.EvaluationStatus)
{
    @PrePersist
    internal fun onCreate() {
        timestamp = Date()
        if (uri.isBlank())
            uri = id.toString()
        status = RetinalEvaluation.EvaluationStatus.PENDING
    }
}
