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

package co.avaldes.retipy.domain.evaluation.optical

import co.avaldes.retipy.domain.diagnostic.Diagnostic
import co.avaldes.retipy.persistence.evaluation.optical.OpticalEvaluationBean
import java.util.*
import kotlin.collections.HashMap

data class OpticalEvaluation(
    var id: Long = 0,
    var version: Long = 1,
    var creationDate: Date = Date(),
    var updateDate: Date = Date(),
    var visualLeftEye: String = "",
    var visualRightEye: String = "",
    var visualLeftPh: String = "",
    var visualRightPh: String = "",
    var pupilLeftEyeRD: Int = 1,
    var pupilLeftEyeRC: Int = 1,
    var pupilLeftEyeDPA: Int = 1,
    var pupilRightEyeRD: Int = 1,
    var pupilRightEyeRC: Int = 1,
    var pupilRightEyeDPA: Int = 1,
    var biomicroscopy: MutableMap<String, String> = emptyMap<String, String>().toMutableMap(),
    var ocularIntraPressure: Int = 10,
    private var diagnostics: List<Diagnostic> = emptyList())
{
    private val diagnosticMap: MutableMap<Long, Diagnostic> = HashMap()

    init
    {
        // remove all biomicroscopy fields that have no value associated with it
        biomicroscopy = biomicroscopy.filterNot { key -> key.value.isBlank() }.toMutableMap()

        // assert that the biomicroscopy contains the obligatory fields:
        for (biomicroscopyField in BIOMICROSCOPY_REQUIRED)
        {
            if (biomicroscopy[biomicroscopyField] == null)
            {
                biomicroscopy[biomicroscopyField] = ""
            }
        }

        this.diagnostics.forEach { diagnosticMap[it.id] = it }
    }

    fun getDiagnostics() : List<Diagnostic> = diagnosticMap.values.toList().sortedBy { record -> record.id }

    fun getDiagnostic(id: Long) : Diagnostic? = diagnosticMap[id]

    fun addDiagnostic(diagnostic: Diagnostic)
    {
        diagnosticMap[diagnostic.id] = diagnostic
    }

    companion object
    {
        val BIOMICROSCOPY_REQUIRED: List<String> = listOf("Cornea", "Iris", "Cristalino", "Camara Anterior")

        private const val BIOMICROSCOPY_SEPARATOR = "#"

        private const val BIOMICROSCOPY_TUPLE_SEPARATOR = "|"

        private fun parseBiomicroscopy(string: String): MutableMap<String, String>
        {
            val output = HashMap<String, String>()
            if (string.isNotBlank())
            {
                string.split(BIOMICROSCOPY_SEPARATOR).forEach{
                    val current = it.split(BIOMICROSCOPY_TUPLE_SEPARATOR)
                    output[current[0]] = current[1]
                }
            }
            return output
        }

        private fun biomicroscopyToString(biomicroscopy: Map<String, String>): String
        {
            val builder = StringBuilder()
            if (biomicroscopy.entries.isNotEmpty())
            {
                biomicroscopy.entries.forEach{
                    builder.append(it.key)
                    builder.append(BIOMICROSCOPY_TUPLE_SEPARATOR)
                    builder.append(it.value)
                    builder.append(BIOMICROSCOPY_SEPARATOR)
                }
                builder.deleteCharAt(builder.lastIndexOf(BIOMICROSCOPY_SEPARATOR))
            }
            return builder.toString()
        }

        fun fromPersistence(opticalEvaluationBean: OpticalEvaluationBean) = OpticalEvaluation(
            opticalEvaluationBean.id,
            opticalEvaluationBean.version,
            opticalEvaluationBean.creationDate,
            opticalEvaluationBean.updateDate,
            opticalEvaluationBean.visualLeftEye,
            opticalEvaluationBean.visualRightEye,
            opticalEvaluationBean.visualLeftPh,
            opticalEvaluationBean.visualRightPh,
            opticalEvaluationBean.pupilLeftEyeRD,
            opticalEvaluationBean.pupilLeftEyeRC,
            opticalEvaluationBean.pupilLeftEyeDPA,
            opticalEvaluationBean.pupilRightEyeRD,
            opticalEvaluationBean.pupilRightEyeRC,
            opticalEvaluationBean.pupilRightEyeDPA,
            parseBiomicroscopy(opticalEvaluationBean.biomicroscopy),
            opticalEvaluationBean.ocularIntraPressure,
            opticalEvaluationBean.diagnostics.map { Diagnostic.fromPersistence(it) })

        fun toPersistence(opticalEvaluation: OpticalEvaluation) = OpticalEvaluationBean(
            opticalEvaluation.id,
            opticalEvaluation.version,
            opticalEvaluation.creationDate,
            opticalEvaluation.updateDate,
            opticalEvaluation.visualLeftEye,
            opticalEvaluation.visualRightEye,
            opticalEvaluation.visualLeftPh,
            opticalEvaluation.visualRightPh,
            opticalEvaluation.pupilLeftEyeRD,
            opticalEvaluation.pupilLeftEyeRC,
            opticalEvaluation.pupilLeftEyeDPA,
            opticalEvaluation.pupilRightEyeRD,
            opticalEvaluation.pupilRightEyeRC,
            opticalEvaluation.pupilRightEyeDPA,
            biomicroscopyToString(opticalEvaluation.biomicroscopy),
            opticalEvaluation.ocularIntraPressure,
            opticalEvaluation.getDiagnostics().map { Diagnostic.toPersistence(it) })
    }
}

