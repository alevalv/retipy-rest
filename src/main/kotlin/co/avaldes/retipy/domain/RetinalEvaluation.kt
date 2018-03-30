package co.avaldes.retipy.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="RetinalEvaluation")
data class RetinalEvaluation(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long? = null,
        val resource_url: String,
        val data: String)
{
    override fun toString(): String = "Evaluation($id, $resource_url)"
}

