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

package co.avaldes.retipy.security.persistence.user

const val ROLE_ADMINISTRATOR = "ROLE_ADMINISTRATOR"
const val ROLE_DOCTOR = "ROLE_DOCTOR"
const val ROLE_RESIDENT = "ROLE_RESIDENT"
const val ROLE_WORKER = "ROLE_WORKER"

enum class Role(val authority: String)
{
    Administrator(ROLE_ADMINISTRATOR),
    Doctor(ROLE_DOCTOR),
    Resident(ROLE_RESIDENT),
    Worker(ROLE_WORKER),
}
