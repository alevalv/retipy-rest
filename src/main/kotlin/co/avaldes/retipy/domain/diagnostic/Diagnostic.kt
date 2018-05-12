package co.avaldes.retipy.domain.diagnostic

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(name = "Diagnostics")
data class Diagnostic(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    val image: String,
    val diagnostic: String,
    val rois: String,
    val notes: String,
    var status: DiagnosticStatus,
    @Temporal(TemporalType.TIMESTAMP) var creationDate: Date,
    @Temporal(TemporalType.TIMESTAMP) var updateDate: Date)
{
    @PrePersist
    internal fun onCreate()
    {
        status = DiagnosticStatus.CREATED
        creationDate = Date()
    }

    @PreUpdate
    internal fun onUpdate()
    {
        updateDate = Date()
    }
}
