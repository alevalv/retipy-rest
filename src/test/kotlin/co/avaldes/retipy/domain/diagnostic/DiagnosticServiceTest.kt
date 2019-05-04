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

import co.avaldes.retipy.persistence.diagnostic.DiagnosticBean
import co.avaldes.retipy.persistence.diagnostic.DiagnosticStatus
import co.avaldes.retipy.persistence.diagnostic.IDiagnosticRepository
import co.avaldes.retipy.rest.common.IncorrectInputException
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date
import java.util.Optional

/**
 * Test class for [DiagnosticService].
 */
internal class DiagnosticServiceTest {
    private val diagnosticId = 123L

    private lateinit var diagnosticService: DiagnosticService
    private val mockDiagnosticRepository: IDiagnosticRepository = mockk(relaxed = true)
    private val diagnosticBean = DiagnosticBean(
        diagnosticId, "", "", "", DiagnosticStatus.Created, Date(), Date())

    @BeforeEach
    fun setUp() {
        clearMocks(mockDiagnosticRepository)

        every { mockDiagnosticRepository.findById(diagnosticId) } returns Optional.of(diagnosticBean)

        diagnosticService = DiagnosticService(mockDiagnosticRepository)
    }

    @Test
    fun find() {
        val diagnostic = diagnosticService.find(diagnosticId)
        assertNotNull(diagnostic, "diagnostic cannot be null")
        assertEquals(diagnostic!!.id, diagnosticId, "diagnostic id does not match")
    }

    @Test
    fun find_notfound() {
        val diagnostic = diagnosticService.find(7486435)
        assertNull(diagnostic, "diagnostic must be null")
    }

    @Test
    fun get() {
        val diagnostic = diagnosticService.get(diagnosticId)
        assertEquals(diagnostic.id, diagnosticId, "diagnostic id does not match")
    }

    @Test
    fun get_notfound() {
        assertThrows(IncorrectInputException::class.java) { diagnosticService.get(7486435) }
    }

    @Test
    fun delete() {
        diagnosticService.delete(diagnosticId)
        verify { mockDiagnosticRepository.deleteById(diagnosticId) }
    }
}
