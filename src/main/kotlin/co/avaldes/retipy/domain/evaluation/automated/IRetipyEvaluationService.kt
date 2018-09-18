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

package co.avaldes.retipy.domain.evaluation.automated

import co.avaldes.retipy.common.ICRUDService
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus

interface IRetipyEvaluationService: ICRUDService<RetipyEvaluation>
{
    fun findByDiagnostic(diagnosticId: Long): List<RetipyEvaluation>

    /**
     * Creates a new Pending evaluation for the given diagnostic for the given name.
     */
    fun fromDiagnostic(diagnosticId: Long, task: RetipyTask): RetipyEvaluation

    /**
     * Gets all [RetipyEvaluation] that are marked as pending.
     */
    fun getPendingEvaluations(): List<RetipyEvaluation>

    /**
     * Get all [RetipyEvaluation] that has the given [status].
     *
     * @param status a [RetipyEvaluationStatus].
     *
     * @return a [List] of [RetipyEvaluation], will return empty if no evaluation is found.
     */
    fun getEvaluationsByStatus(status: RetipyEvaluationStatus): List<RetipyEvaluation>
}
