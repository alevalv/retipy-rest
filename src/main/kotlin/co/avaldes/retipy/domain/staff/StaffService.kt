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
import co.avaldes.retipy.persistence.staff.DoctorAssignedResidentsBean
import co.avaldes.retipy.persistence.staff.IDoctorAssignedResidentsRepository
import co.avaldes.retipy.rest.common.IncorrectInputException
import co.avaldes.retipy.security.domain.user.IUserService
import co.avaldes.retipy.security.persistence.user.Role
import org.springframework.stereotype.Service

@Service
internal class StaffService(
    private val doctorResidentsRepository: IDoctorAssignedResidentsRepository,
    private val userService: IUserService
) : IStaffService {
    override fun getDoctorAssignedResidents(doctorId: Long): List<Person> {
        val doctor = userService.get(doctorId)
        if (!doctor.roles.contains(Role.Doctor)) {
            throw IncorrectInputException("Invalid doctor id")
        }
        val assignedResidents = ArrayList<Person>()
        val doctorResidents = doctorResidentsRepository.findById(doctorId)
        if (doctorResidents.isPresent)
            doctorResidents.get().residentIds.forEach {
                val resident = userService.find(it)
                if (resident != null) {
                    if (resident.roles.contains(Role.Resident)) {
                        assignedResidents.add(Person.fromUser(resident))
                    }
                }
            }
        return assignedResidents
    }

    override fun setDoctorAssignedResidents(doctorId: Long, residentIdList: List<Long>) {
        val doctor = userService.get(doctorId)
        if (!doctor.roles.contains(Role.Doctor)) {
            throw IncorrectInputException("Invalid doctor id")
        }
        val filteredResidentList = residentIdList.filter {
            val resident = userService.find(it)
            resident != null && resident.roles.contains(Role.Resident)
        }
        doctorResidentsRepository.save(DoctorAssignedResidentsBean(doctorId, filteredResidentList))
    }

    override fun getDoctorsFromResident(residentId: Long): List<Long> {
        return doctorResidentsRepository.findDoctorIdByResidentId(residentId).map { it.doctorId }
    }
}
