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

package co.avaldes.retipy.security.domain.user

import co.avaldes.retipy.security.persistence.user.Roles
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Test class for [User]
 */
internal class UserTest
{
    private val id = 111L
    private val identity = "123456"
    private val name = "A name"
    private val username = "ausername"
    private val password = "a password"
    private val roles = mutableSetOf(Roles.Administrator, Roles.Doctor)
    private val enabled = true
    private val locked = false
    private val expired = false

    private lateinit var testInstance: User

    @BeforeEach
    fun setUp()
    {
        testInstance = User(id, identity, name, username, password, roles, enabled, locked, expired)
    }

    @Test
    fun getAuthorities()
    {
        val authorities = roles.map { SimpleGrantedAuthority(it.authority) }
        Assert.assertEquals("authorities does not match", authorities, testInstance.authorities)
    }

    @Test
    fun test_mappers()
    {
        val bean = User.toPersistence(testInstance)
        val mappedObject = User.fromPersistence(bean)
        Assert.assertEquals("mapped object does not match", testInstance, mappedObject)
    }
}
