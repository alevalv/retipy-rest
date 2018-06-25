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

import co.avaldes.retipy.security.persistence.user.IUserRepository
import co.avaldes.retipy.security.persistence.user.UserBean
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

internal class UserServiceTest
{
    private val userId : Long = 1
    private val username = "beanu"
    private val userBean = UserBean(userId, "111111", "bean", username, "beanp", true, false, false)

    private val mockUserRepository: IUserRepository = mock<IUserRepository>
        {
            on { findByUsername(any()) } doReturn userBean
        }
    private val test : UserService = UserService(mockUserRepository)

    @BeforeEach
    fun setUp()
    {
        whenever(mockUserRepository.findById(userId)).thenReturn(Optional.of(userBean))
        whenever(mockUserRepository.findByUsername(username)).thenReturn(userBean)
    }

    @Test
    fun findById()
    {
        val user = test.findById(userId)
        Assertions.assertEquals(userId, user!!.id, "user id does not match")
    }

    @Test
    fun findByUsername()
    {
        val user = test.findByUsername(username)
        Assertions.assertEquals(username, user!!.username, "username does not match")
    }

    @Test
    fun createUser()
    {
        whenever(mockUserRepository.save<UserBean>(any())).thenReturn(userBean)
        whenever(mockUserRepository.findByUsername(username)).thenReturn(null)
        val savedUser = test.createUser(
            User(0, username, username, username, username, true, false , false))
        verify(mockUserRepository).save<UserBean>(any())
        Assertions.assertEquals(
            savedUser, User.fromPersistence(userBean), "returned user object does not match")
    }

    @Test
    fun createUser_Exception()
    {
        assertThrows<IllegalArgumentException> {
            test.createUser(
                User(userId, username, username, username, username, true, false , false)) }
    }

    @Test
    fun delete()
    {
        test.delete(User.fromPersistence(userBean))
        verify(mockUserRepository).delete(any())
    }
}
