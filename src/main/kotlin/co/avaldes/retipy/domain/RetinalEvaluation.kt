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

@Entity
@Table(name="RetinalEvaluation")
data class RetinalEvaluation(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "RE_ID")
        val id: Long,
        val uri: String,
        @Temporal(TemporalType.TIMESTAMP) var timestamp: Date,
        @Lob val data: String)
{
    @PrePersist
    internal fun onCreate() {
        timestamp = Date()
    }

    override fun toString(): String = "Evaluation($id, $uri)"
}

