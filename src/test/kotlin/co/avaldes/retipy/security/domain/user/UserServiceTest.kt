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

import co.avaldes.retipy.common.NoOpPasswordEncoder
import co.avaldes.retipy.rest.common.IncorrectInputException
import co.avaldes.retipy.security.persistence.user.IUserRepository
import co.avaldes.retipy.security.persistence.user.UserBean
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

internal class UserServiceTest
{
    private val userId : Long = 1
    private val username = "beanu"
    private val userBean = UserBean(userId, "111111", "bean", username, "beanp", "",true, false, false)

    private val mockUserRepository: IUserRepository = mockk(relaxed = true)
    private lateinit var testInstance : UserService

    @BeforeEach
    fun init()
    {
        clearMocks(mockUserRepository)
        testInstance = UserService(mockUserRepository, NoOpPasswordEncoder())
        every { mockUserRepository.findByUsername(any()) } returns userBean
        every { mockUserRepository.findById(any()) } returns Optional.of(userBean)
    }

    @Test
    fun findById()
    {
        val user = testInstance.find(userId)
        Assertions.assertEquals(userId, user!!.id, "user id does not match")
    }

    @Test
    fun findByUsername()
    {
        val user = testInstance.findByUsername(username)
        Assertions.assertEquals(username, user!!.username, "username does not match")
    }

    @Test
    fun createUser()
    {
        every { mockUserRepository.save<UserBean>(any()) } returns userBean
        every { mockUserRepository.findByUsername(username) } returns null
        val savedUser = testInstance.createUser(
            User(0, username, username, username, username, mutableSetOf(),true, false , false))
        verify(exactly = 1) { mockUserRepository.save<UserBean>(any()) }
        Assertions.assertEquals(
            savedUser, User.fromPersistence(userBean), "returned user object does not match")
    }

    @Test
    fun createUser_Exception()
    {
        assertThrows<IncorrectInputException> {
            testInstance.createUser(
                User(userId, username, username, username, username, mutableSetOf(),true, false , false)) }
    }

    @Test
    fun delete()
    {
        testInstance.delete(User.fromPersistence(userBean))
        verifySequence { mockUserRepository.delete(any()) }
    }
}
