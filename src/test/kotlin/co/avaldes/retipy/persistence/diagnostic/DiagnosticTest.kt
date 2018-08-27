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

package co.avaldes.retipy.persistence.diagnostic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class DiagnosticTest
{
    private val id:Long = 100L
    private val image = "a text image?"
    private val diagnostic = "some diagnostic"
    private val rois = "[]"
    private val status = DiagnosticStatus.COMPLETED
    private val creationDate = Date()
    private val updateDate = Date()

    private val testInstance =
        DiagnosticBean(id, image, diagnostic, rois, status, creationDate, updateDate)

    @Test
    fun test_onCreate()
    {
        testInstance.onCreate()
        assertNotEquals(creationDate, testInstance.creationDate)
        assertEquals(DiagnosticStatus.CREATED, testInstance.status)
    }

    @Test
    fun test_onUpdate()
    {
        testInstance.onUpdate()
        assertNotEquals(updateDate, testInstance.updateDate)
    }
}
