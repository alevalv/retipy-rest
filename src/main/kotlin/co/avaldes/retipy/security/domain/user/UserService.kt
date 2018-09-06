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

import co.avaldes.retipy.rest.common.IncorrectInputException
import co.avaldes.retipy.security.domain.common.NoOpPasswordValidator
import co.avaldes.retipy.security.persistence.user.IUserRepository
import co.avaldes.retipy.security.persistence.user.Roles
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * Implementation of [IUserService].
 */
@Service
internal class UserService(
    private val userRepository: IUserRepository,
    private val passwordEncoder: PasswordEncoder) : IUserService
{
    private val passwordValidator = NoOpPasswordValidator()

    override fun find(id: Long): User?
    {
        val bean = userRepository.findById(id)
        var user : User? = null
        if (bean.isPresent)
            user = User.fromPersistence(bean.get())
        return user

    }

    override fun findByUsername(username: String): User?
    {
        var user: User? = null
        if (!username.isBlank())
        {
            val bean = userRepository.findByUsername(username)
            if (bean != null)
            {
                user = User.fromPersistence(bean)
            }
        }
        return user
    }

    override fun delete(obj: User)
    {
        userRepository.delete(User.toPersistence(obj))
    }

    override fun createUser(user: User): User
    {
        if (user.id != 0L || this.findByUsername(user.username) != null)
        {
            throw IncorrectInputException("New user is invalid")
        }
        passwordValidator.validatePassword(user.password)
        val newUser = User(
            0L,
            user.identity,
            user.name,
            user.username,
            passwordEncoder.encode(user.password),
            mutableSetOf(Roles.Resident),
            false,
            false,
            false)

        return save(newUser)
    }

    override fun updatePassword(user: User, newPassword: String): User
    {
        val storedUser = get(user.id)
        passwordValidator.validatePassword(newPassword)
        return save(User(
            storedUser.id,
            storedUser.identity,
            storedUser.name,
            storedUser.username,
            passwordEncoder.encode(newPassword),
            storedUser.roles,
            storedUser.enabled,
            storedUser.locked,
            false))
    }

    override fun updateName(user: User, name: String): User
    {
        val storedUser = get(user.id)
        return save(User(
            storedUser.id,
            storedUser.identity,
            name,
            storedUser.username,
            storedUser.password,
            storedUser.roles,
            storedUser.enabled,
            storedUser.locked,
            false))
    }

    override fun login(username: String, password: String): User?
    {
        var login : User? = null
        val persistedUser = findByUsername(username)
        if (persistedUser != null
            && persistedUser.enabled
            && !persistedUser.expired
            && !persistedUser.locked
            && passwordEncoder.matches(password, persistedUser.password))
        {
            login = persistedUser
        }
        return login
    }

    override fun save(obj: User): User
    {
        val savedBean = userRepository.save(User.toPersistence(obj))
        return User.fromPersistence(savedBean)
    }

    override fun get(id: Long): User =
        find(id) ?: throw IncorrectInputException("User with id $id not found")

    override fun delete(id: Long)
    {
        userRepository.deleteById(id)
    }
}
