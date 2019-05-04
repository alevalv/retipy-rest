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

import co.avaldes.retipy.domain.common.roi.Roi
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RoiTest {
    private val roi_x = listOf(1, 3, 4, 5, 6)
    private val roi_y = listOf(1, 2, 4, 7, 6)
    private val notes = "this is a test roi"
    private val color = "black"

    private lateinit var testInstance: Roi

    @BeforeEach
    fun setUp() {
        testInstance = Roi(roi_x, roi_y, notes, color)
    }

    @Test
    fun test_toString() {
        Assertions.assertEquals(
            "{\"x\":[1,3,4,5,6],\"y\":[1,2,4,7,6],\"notes\":\"this is a test roi\",\"color\":\"black\"}",
            testInstance.toString())
    }

    @Test
    fun test_persistence() {
        val rois = listOf(testInstance, testInstance, testInstance)
        val persistedRois = Roi.toPersistence(rois)
        val fromPersistenceRois = Roi.fromPersistence(persistedRois)
        Assertions.assertEquals(rois, fromPersistenceRois)
    }

    @Test
    fun test_emptyRoiMapper() {
        val bean = Roi.toPersistence(listOf())
        val domain = Roi.fromPersistence(bean)

        Assert.assertTrue(domain.isEmpty())
    }
}
