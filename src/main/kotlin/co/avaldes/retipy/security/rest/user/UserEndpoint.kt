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

import co.avaldes.retipy.security.domain.user.IUserService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * REST endpoint to handle User CRUD operations and login.
 * The login operation should return an encrypted Jason Web Token (JWT).
 *
 * All operations are available in /retipy/user resource
 */
@CrossOrigin
@RestController
internal class UserEndpoint(private val userService: IUserService)
{
    @PostMapping("/retipy/user/login")
    fun login(@RequestBody userLoginDTO: UserLoginDTO): UserLoginDTO
    {
        val user = userService.login(userLoginDTO.username, userLoginDTO.password)
            ?: throw IllegalArgumentException("Incorrect username or password")
        return UserLoginDTO(user.username, user.identity, "", user.name, Date())
    }

    @PostMapping("/retipy/user")
    fun createUser(@RequestBody userLoginDTO: UserLoginDTO): UserLoginDTO
    {
        val user = userService.createUser(UserLoginDTO.toDomain(userLoginDTO))
        return UserLoginDTO.fromDomain(user)
    }

    @PostMapping("/retipy/user/password")
    fun updatePassword(@RequestBody userLoginDTO: UserLoginDTO): UserLoginDTO
    {
        val user = userService.updatePassword(
            UserLoginDTO.toDomain(userLoginDTO), userLoginDTO.password)
        return UserLoginDTO.fromDomain(user)
    }
}
