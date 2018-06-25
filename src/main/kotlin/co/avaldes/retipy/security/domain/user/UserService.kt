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

import co.avaldes.retipy.security.domain.common.NoOpPasswordValidator
import co.avaldes.retipy.security.persistence.user.IUserRepository
import org.springframework.stereotype.Service

/**
 * Implementation of [IUserService].
 */
@Service
internal class UserService(
    private val userRepository: IUserRepository) : IUserService
{
    private val passwordValidator = NoOpPasswordValidator()

    override fun findById(id: Long): User?
    {
        val bean = userRepository.findById(id)
        var user : User? = null
        if (bean.isPresent)
            user = User.fromPersistence(bean.get())
        return user

    }

    override fun findByUsername(username: String): User?
    {
        val bean = userRepository.findByUsername(username)
        return if (bean != null) User.fromPersistence(bean) else null
    }

    override fun delete(user: User)
    {
        userRepository.delete(User.toPersistence(user))
    }

    override fun createUser(user: User): User
    {
        if (user.id != 0L || this.findByUsername(user.username) != null)
        {
            throw IllegalArgumentException("New user data is invalid")
        }
        passwordValidator.validatePassword(user.password)
        val newUser = User(
            0L,
            user.identity,
            user.name,
            user.username,
            user.password,
            true,
            false,
            false)

        return save(newUser)
    }

    override fun updatePassword(user: User, newPassword: String): User
    {
        val storedUser = findById(user.id) ?: throw IllegalArgumentException("Invalid user")
        passwordValidator.validatePassword(newPassword)
        return save(User(
            storedUser.id,
            storedUser.identity,
            storedUser.name,
            storedUser.username,
            newPassword,
            storedUser.enabled,
            storedUser.locked,
            false))
    }

    override fun updateName(user: User, name: String): User
    {
        val storedUser = findById(user.id) ?: throw IllegalArgumentException("Invalid user")
        return save(User(
            storedUser.id,
            storedUser.identity,
            name,
            storedUser.username,
            storedUser.password,
            storedUser.enabled,
            storedUser.locked,
            false))
    }

    override fun login(username: String, password: String): User?
    {
        var login : User? = null
        val persistedUser = findByUsername(username)
        if (persistedUser != null && password == persistedUser.password)
        {
            login = persistedUser
        }
        return login
    }

    private fun save(user: User): User
    {
        val savedBean = userRepository.save(User.toPersistence(user))
        return User.fromPersistence(savedBean)
    }

}
