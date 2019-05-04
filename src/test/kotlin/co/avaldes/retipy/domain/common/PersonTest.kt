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

package co.avaldes.retipy.domain.common

import co.avaldes.retipy.security.domain.user.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PersonTest {
    private val name = "a name"
    private val identity = "991h31"
    private val id = 8123L
    private val user = User(id, identity, name, "username", "password")

    private lateinit var person: Person

    @BeforeEach
    fun setUp() {
        person = Person(id, identity, name)
    }

    @Test
    fun fromPerson() {
        assertEquals(person, Person.fromUser(user), "Person object does not match")
    }
}
