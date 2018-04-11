package co.avaldes.retipy.domain

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.PrePersist
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

enum class EvaluationStatus
{
    PENDING, COMPLETE, ERROR
}

@Entity
@Table(name="RetinalEvaluation")
data class RetinalEvaluation(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
        var uri: String,
        @Temporal(TemporalType.TIMESTAMP) var timestamp: Date,
        @Lob var data: String,
        @Lob val image: String,
        var status: EvaluationStatus)
{
    @PrePersist
    internal fun onCreate() {
        timestamp = Date()
        status = EvaluationStatus.PENDING
    }

    override fun toString(): String = "Evaluation($id, $uri)"
}

