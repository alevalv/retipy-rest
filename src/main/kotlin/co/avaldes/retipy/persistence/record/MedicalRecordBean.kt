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

package co.avaldes.retipy.persistence.record

import co.avaldes.retipy.domain.record.Education
import co.avaldes.retipy.domain.record.Sex
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob

@Entity
data class MedicalRecordBean(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long,
    // DEMOGRAPHY DATA
    val identity: Long,
    val name: String,
    val age: Int,
    val sex: Sex,
    val origin: String,
    val procedencia: String,
    val education: Education,
    val race: String,
    @Lob
    val patologicalPast: String,
    @Lob
    val familiarPast: String,
    @Lob
    val medicines: String,
    // OPHTHALMOLOGIST DATA
    val visualLeftEye: String,
    val visualRightEye: String,
    val visualLeftPh: String,
    val visualRightPh: String,
    val pupilLeftEyeRD: Int,
    val pupilLeftEyeRC: Int,
    val pupilLeftEyeDPA: Int,
    val pupilRightEyeRD: Int,
    val pupilRightEyeRC: Int,
    val pupilRightEyeDPA: Int,
    val biomicroscopy: String,
    val PIO: String,
    val evaluationId: Long
    )
