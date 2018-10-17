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

package co.avaldes.retipy.domain.staff

import co.avaldes.retipy.domain.common.Person

/**
 * Interface that exposes operations to handle extra data related with the users.
 */
interface IStaffService
{
    /**
     * Gets the list of residents that are assigned to the given doctor.
     */
    fun getDoctorAssignedResidents(doctorId: Long): List<Person>

    /**
     * Sets the given list of residents to the given doctor.
     */
    fun setDoctorAssignedResidents(doctorId: Long, residentIdList: List<Long>)

    /**
     * Gets the doctors which have the given resident assigned to them.
     */
    fun getDoctorsFromResident(residentId: Long): List<Long>
}
