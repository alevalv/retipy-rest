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

package co.avaldes.retipy.security.domain

import co.avaldes.retipy.security.domain.user.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationFilter(
    @Value("\${retipy.auth.header}") val header:String,
    val userDetailsService: UserDetailsServiceImpl,
    val tokenService: TokenService) : OncePerRequestFilter()
{
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain)
    {
        try
        {
            val username = tokenService.getTokenUsername(getAuthenticationToken(request))
            if (username.isNotBlank())
            {
                val user = userDetailsService.loadUserByUsername(username)
                val passwordAuthenticationToken = UsernamePasswordAuthenticationToken(user, null, user.authorities)
                passwordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = passwordAuthenticationToken
            }
        }
        catch (e: UsernameNotFoundException) {}
        filterChain.doFilter(request, response)
    }

    private fun getAuthenticationToken(request: HttpServletRequest): String
    {
        return request.getHeader(header) ?: ""

    }
}
