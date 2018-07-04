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

import co.avaldes.retipy.persistence.record.IMedicalRecordRepository
import org.springframework.stereotype.Service

@Service
internal class MedicalRecordService(
    private val medicalRecordRepository: IMedicalRecordRepository): IMedicalRecordService
{
    fun getRecord(id:Long) : MedicalRecord?
    {
        val bean = medicalRecordRepository.findById(id)
        return if (bean.isPresent) MedicalRecord.fromPersistence(bean.get()) else null
    }
}
