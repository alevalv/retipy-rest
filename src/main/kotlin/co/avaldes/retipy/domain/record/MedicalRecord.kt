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

package co.avaldes.retipy.domain.record

import co.avaldes.retipy.persistence.record.MedicalRecordBean

enum class Sex
{
    Male,
    Female
}

enum class Education
{
    None,
    Primary,
    HighSchool,
    Bachelor,
    Master,
    Doctorate
}

data class Demography(
    val identity: Long,
    val name: String,
    val age: Int,
    val sex: Sex,
    val origin: String,
    val procedencia: String,
    val education: Education,
    val race: String,
    val patologicalPast: List<String>,
    val familiarPast: List<String>,
    val medicines: List<String>)

data class OpticalEvaluation(
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

data class MedicalRecord(
    val id: Long,
    val demography: Demography,
    val opticalEvaluation: OpticalEvaluation)
{
    companion object
    {
        const val TOKEN = "|"

        fun fromPersistence(medicalRecordBean: MedicalRecordBean) = MedicalRecord(
            medicalRecordBean.id,
            Demography(
                medicalRecordBean.identity,
                medicalRecordBean.name,
                medicalRecordBean.age,
                medicalRecordBean.sex,
                medicalRecordBean.origin,
                medicalRecordBean.procedencia,
                medicalRecordBean.education,
                medicalRecordBean.race,
                parseListFromString(medicalRecordBean.patologicalPast),
                parseListFromString(medicalRecordBean.familiarPast),
                parseListFromString(medicalRecordBean.medicines)),
            OpticalEvaluation(
                medicalRecordBean.visualLeftEye,
                medicalRecordBean.visualRightEye,
                medicalRecordBean.visualLeftPh,
                medicalRecordBean.visualRightPh,
                medicalRecordBean.pupilLeftEyeRD,
                medicalRecordBean.pupilLeftEyeRC,
                medicalRecordBean.pupilLeftEyeDPA,
                medicalRecordBean.pupilRightEyeRD,
                medicalRecordBean.pupilRightEyeRC,
                medicalRecordBean.pupilRightEyeDPA,
                medicalRecordBean.biomicroscopy,
                medicalRecordBean.PIO,
                medicalRecordBean.evaluationId))

        private fun parseListFromString(string:String): List<String>
        {
            return string.split(TOKEN).toList()
        }

        private fun parseListToString(list:List<String>): String
        {
            val builder = StringBuilder()
            val last = list[list.size -1 ]
            list.dropLast(1).forEach { builder.append(it)
                builder.append(TOKEN)
            }
            builder.append(last)
            return builder.toString()
        }
    }
}

