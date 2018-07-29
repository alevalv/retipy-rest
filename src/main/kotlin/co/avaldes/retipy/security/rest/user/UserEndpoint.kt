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

import co.avaldes.retipy.security.domain.TokenService
import co.avaldes.retipy.security.domain.user.IUserService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * REST endpoint to handle User CRUD operations and login.
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

    @PostMapping("/retipy/user/login")
    fun login(@RequestBody loginRequest: LoginRequest): String
    {
        val user = userService.login(loginRequest.username, loginRequest.password)
            ?: throw UsernameNotFoundException("Incorrect username or password")
        return tokenService.createToken(user)
    }

    @PostMapping("retipy/user/token")
    fun isTokenValid(@RequestBody token: String): Boolean
    {
        return tokenService.isTokenValid(token)
    }

    @PostMapping("/retipy/user")
    fun createUser(@RequestBody userDTO: UserDTO): UserDTO
    {
        val user = userService.createUser(UserDTO.toDomain(userDTO))
        return UserDTO.fromDomain(user)
    }

    @PostMapping("/retipy/user/enable")
    fun enableUser(@RequestBody username:String): Boolean
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
        return success
    }

    @PostMapping("/retipy/user/password")
    fun updatePassword(@RequestBody userDTO: UserDTO): UserDTO
    {
        val user = userService.updatePassword(
            UserDTO.toDomain(userDTO), userDTO.password)
        return UserDTO.fromDomain(user)
    }
}
