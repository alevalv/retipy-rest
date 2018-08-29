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

package co.avaldes.retipy.domain.evaluation.optical

import co.avaldes.retipy.domain.diagnostic.Diagnostic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class OpticalEvaluationTest
{
    private lateinit var testInstance: OpticalEvaluation

    @BeforeEach
    fun setUp()
    {
        testInstance = OpticalEvaluation(
            1, 1,
            Date(),
            Date(),
            "",
            "",
            "",
            "",
            0,
            0,
            0,
            0,
            0,
            0,
            mutableMapOf(Pair("Iris", "some")),
            "",
            listOf(Diagnostic()))
    }

    @Test
    fun verifyConstructor()
    {
        for (opticalEvaluationField in OpticalEvaluation.BIOMICROSCOPY_REQUIRED)
        {
            Assertions.assertNotNull(
                testInstance.biomicroscopy[opticalEvaluationField],
                "$opticalEvaluationField cannot be null")
        }
    }

    @Test
    fun mappers()
    {
        val opticalEvaluationBean = OpticalEvaluation.toPersistence(testInstance)
        val opticalEvaluation = OpticalEvaluation.fromPersistence(opticalEvaluationBean)
        Assertions.assertEquals(testInstance, opticalEvaluation, "mapping failed")
    }
}
