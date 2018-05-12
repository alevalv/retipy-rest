/*
 * Copyright (C) 2018 - Alejandro Valdes
 *
 * This file is part of retipy.
 *
 * retipy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * retipy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with retipy.  If not, see <http://www.gnu.org/licenses/>.
 */

package co.avaldes.retipy.persistence

import co.avaldes.retipy.domain.Results
import co.avaldes.retipy.domain.evaluation.RetinalEvaluation
import java.util.*
import javax.persistence.ElementCollection
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
