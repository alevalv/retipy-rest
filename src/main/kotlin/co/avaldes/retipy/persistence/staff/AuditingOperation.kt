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

package co.avaldes.retipy.persistence.staff

enum class AuditingOperation(val operation: String)
{
    PatientRead("Patient Opened"),
    PatientEdit("Patient Modified"),
    OpticalEvaluationRead("Optical Evaluation Opened"),
    OpticalEvaluationEdit("Optical Evaluation Modified"),
    OpticalEvaluationDelete("Optical Evaluation Deleted"),
    DiagnosticRead("Diagnostic Opened"),
    DiagnosticEdit("Diagnostic Modified"),
    DiagnosticDelete("Diagnostic Deleted"),
}