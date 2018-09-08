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

package co.avaldes.retipy.domain.evaluation.automated

import co.avaldes.retipy.persistence.evaluation.retinal.RetinalEvaluationStatus
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class RetipyEvaluationTest
{
    private lateinit var retipyEvaluation: RetipyEvaluation

    @BeforeEach
    fun setUp()
    {
        retipyEvaluation = RetipyEvaluation(
            123L,
            123545L,
            "a evaluation",
            "a image",
            listOf(),
            RetinalEvaluationStatus.COMPLETE,
            Date(),
            Date())
    }

    @Test
    fun test_mapping()
    {
        val bean = RetipyEvaluation.toPersistence(retipyEvaluation)
        val domain = RetipyEvaluation.fromPersistence(bean)
        Assert.assertEquals("mapping failed for RetipyEvaluation", retipyEvaluation, domain)
    }
}
