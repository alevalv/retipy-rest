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

package co.avaldes.retipy.persistence.diagnostic

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(name = "diagnostic")
data class DiagnosticBean(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @Lob val image: String,
    val diagnostic: String,
    @Lob val rois: String,
    var status: DiagnosticStatus,
    @Temporal(TemporalType.TIMESTAMP) var creationDate: Date,
    @Temporal(TemporalType.TIMESTAMP) var updateDate: Date)
{
    @PrePersist
    internal fun onCreate()
    {
        status = DiagnosticStatus.Created
        creationDate = Date()
    }

    @PreUpdate
    internal fun onUpdate()
    {
        updateDate = Date()
    }
}
