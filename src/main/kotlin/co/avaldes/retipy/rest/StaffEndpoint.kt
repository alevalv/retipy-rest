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

package co.avaldes.retipy.rest

import co.avaldes.retipy.rest.dto.patient.PersonDTO
import co.avaldes.retipy.security.domain.user.IUserService
import co.avaldes.retipy.security.persistence.user.Role
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Endpoint to query staff that is registered as a User in the retipy repository.
 */
@CrossOrigin
@RestController
internal class StaffEndpoint(private val userService: IUserService)
{
    @GetMapping("/retipy/staff/doctor")
    fun getDoctors(): List<PersonDTO>
    {
        return userService.getUsersByRole(Role.Doctor).map { PersonDTO.fromDomain(it) }
    }

    @GetMapping("/retipy/staff/resident")
    fun getResidents(): List<PersonDTO>
    {
        return userService.getUsersByRole(Role.Resident).map { PersonDTO.fromDomain(it) }
    }
}
