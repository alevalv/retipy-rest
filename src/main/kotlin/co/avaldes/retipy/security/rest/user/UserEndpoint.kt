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

package co.avaldes.retipy.security.rest.user

import co.avaldes.retipy.rest.common.IncorrectInputException
import co.avaldes.retipy.security.domain.TokenService
import co.avaldes.retipy.security.domain.user.IUserService
import co.avaldes.retipy.security.persistence.user.ROLE_ADMINISTRATOR
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * REST uri to handle User CRUD operations and login.
 * The login operation should return an encrypted Jason Web Token (JWT).
 *
 * All operations are available in /retipy/user resource
 */
@CrossOrigin
@RestController
internal class UserEndpoint(
    private val userService: IUserService, private val tokenService: TokenService)
{
    internal data class LoginRequest(val username: String, val password: String)
    internal data class PasswordChangeRequestDTO(val oldPassword: String, val newPassword: String)

    /**
     * Unsecured uri
     */
    @PostMapping("/retipy/user/login")
    fun login(@RequestBody loginRequest: LoginRequest): String
    {
        val user = userService.login(loginRequest.username, loginRequest.password)
            ?: throw UsernameNotFoundException("Incorrect username or password")
        return tokenService.createToken(user)
    }

    /**
     * Unsecured uri
     */
    @PostMapping("retipy/user/token")
    fun renewToken(@RequestBody token: String): String
    {
        return tokenService.renewToken(token)
    }

    @GetMapping("/retipy/user")
    fun getCurrentUser(): UserDTO
    {
        val user = userService.getCurrentAuthenticatedUser()
        if (user != null)
        {
            return UserDTO.fromDomain(user)
        }
        else
        {
            throw IncorrectInputException("username not found")
        }
    }

    @PostMapping("/retipy/user")
    fun updateUser(@RequestBody userDTO: UserDTO): UserDTO
    {
        val user = userService.getCurrentAuthenticatedUser()
        if (user != null)
        {
            if (user.username != userDTO.username)
            {
                throw IncorrectInputException("cannot modify other users")
            }
            else
            {
                if (userDTO.identity != user.identity)
                    user.identity = userDTO.identity
                if (userDTO.name != user.name)
                    user.name = userDTO.name
                return UserDTO.fromDomain(userService.save(user))
            }
        }
        else
        {
            throw IncorrectInputException("username not found")
        }
    }

    @Secured(ROLE_ADMINISTRATOR)
    @PostMapping("/retipy/user/new")
    fun createUser(@RequestBody userDTO: UserDTO): UserDTO
    {
        val user = userService.createUser(UserDTO.toDomain(userDTO))
        return UserDTO.fromDomain(user)
    }

    @Secured(ROLE_ADMINISTRATOR)
    @PostMapping("/retipy/user/enable")
    fun enableUser(@RequestBody username:String): ResponseEntity<Any>
    {
        val user = userService.findByUsername(username)
        var success = false
        if (user != null && !user.enabled)
        {
            user.enabled = true
            user.expired = false
            user.locked = false
            userService.save(user)
            success = true
        }
        return if (success) ResponseEntity.ok().build() else ResponseEntity.badRequest().build()
    }

    @PostMapping("/retipy/user/password")
    fun updatePassword(@RequestBody passwordChangeRequestDTO: PasswordChangeRequestDTO)
    {
        val user = userService.getCurrentAuthenticatedUser()
        if (user != null)
        {
            if (userService.login(user.username, passwordChangeRequestDTO.oldPassword) != null)
            {
                userService.updatePassword(user, passwordChangeRequestDTO.newPassword)
            }
            else
            {
                throw IncorrectInputException("old password does not match")
            }
        }
        else
        {
            throw IncorrectInputException("username not found")
        }
    }

}
