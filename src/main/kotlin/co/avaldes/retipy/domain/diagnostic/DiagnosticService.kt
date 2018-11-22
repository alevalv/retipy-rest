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

package co.avaldes.retipy.domain.diagnostic

import co.avaldes.retipy.domain.evaluation.automated.IRetipyEvaluationService
import co.avaldes.retipy.persistence.diagnostic.IDiagnosticRepository
import co.avaldes.retipy.rest.common.IncorrectInputException
import org.springframework.stereotype.Service

@Service
internal class DiagnosticService(
    private val diagnosticRepository: IDiagnosticRepository) : IDiagnosticService
{
    override fun find(id: Long): Diagnostic?
    {
        var diagnostic: Diagnostic? = null
        val bean = diagnosticRepository.findById(id)
        if (bean.isPresent)
        {
            diagnostic = Diagnostic.fromPersistence(bean.get())
        }
        return diagnostic
    }

    override fun get(id: Long): Diagnostic
    {
        return find(id) ?: throw IncorrectInputException("diagnostic with id $id not found")
    }

    override fun delete(id: Long)
    {
        diagnosticRepository.deleteById(id)
    }
}
