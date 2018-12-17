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

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import javax.imageio.ImageIO
import javax.imageio.stream.ImageInputStream

class ImageService
{
    fun toPNG(image: String) = to(image, "png")

    fun toJPG(image: String) = to(image, "jpg")

    /**
     * Opens a base 64 [image], returning a [BufferedImage].
     */
    private fun open(image: String): BufferedImage
    {
        val imageBytes = Base64.getDecoder().decode(image)
        val inputStream = ByteArrayInputStream(imageBytes)
        return inputStream.use { ImageIO.read(inputStream) }
    }

    /**
     * Converts the given [image] to the given [format].
     */
    private fun to(image: String, format: String): String
    {
        val image = open(image)
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(image, format, outputStream)
        return Base64.getEncoder().encodeToString(outputStream.toByteArray())
    }
}
