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

package co.avaldes.retipy.persistence.evaluation.retinal

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Table

@Entity
@Table(name="retipy_evaluation")
data class RetipyEvaluationBean(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    val diagnosticId: Long,
    val name: String,
    @Lob
    val image: String,
    @Lob
    val rois: String,
    var status: RetinalEvaluationStatus = RetinalEvaluationStatus.PENDING,
    var creationDate: Date = Date(),
    var updateDate: Date = Date()
)
{
    @PrePersist
    internal fun onCreate()
    {
        creationDate = Date()
        status = RetinalEvaluationStatus.PENDING
    }

    @PreUpdate
    internal fun onUpdate()
    {
        updateDate = Date()
    }
}
