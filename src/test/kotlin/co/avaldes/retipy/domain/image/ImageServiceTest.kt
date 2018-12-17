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

package co.avaldes.retipy.domain.image

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ImageServiceTest
{
    val image = "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" +
        "xcH/AxTl5N3U+rjRAAAAAElFTkSuQmCC"

    private lateinit var imageService: ImageService

    @BeforeEach
    fun setUp()
    {
        imageService = ImageService()
    }

    @Test
    fun toJPG()
    {
        assertTrue(
            imageService.toJPG(image).isNotBlank(),"an image shuold have been returned")
    }

    @Test
    fun toPNG()
    {
        assertTrue(
            imageService.toPNG(image).isNotBlank(), "an image should have been returned")
    }
}
