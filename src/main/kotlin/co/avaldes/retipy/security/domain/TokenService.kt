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

import co.avaldes.retipy.security.domain.user.User
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class TokenService
{
    private val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val issuer = "retipy"
    fun createToken(user: User): String
    {
        val currentTime = Instant.now()
        val token = Jwts.builder()
            .setIssuer(issuer)
            .setSubject(user.username)
            .claim("identity", user.identity)
            .claim("scope", "users")
            .setIssuedAt(Date.from(currentTime))
            .setExpiration(Date.from(currentTime.plus(48, ChronoUnit.HOURS)))
            .signWith(key)
            .compact()
        return token
    }

    fun renewToken(token: String): String
    {
        val currentTime = Instant.now()
        try
        {
            val claims = Jwts.parser()
                .requireIssuer(issuer)
                .require("scope", "users")
                .setSigningKey(key)
                .parseClaimsJws(token)
            return Jwts.builder()
                .setIssuer(issuer)
                .setClaims(claims.body)
                .setIssuedAt(Date.from(currentTime))
                .setExpiration(Date.from(currentTime.plus(48, ChronoUnit.HOURS)))
                .signWith(key)
                .compact()
        }
        catch(e: SecurityException) {
            throw AccessDeniedException("Invalid Token")
        }
        catch(e: ExpiredJwtException) {
            throw AccessDeniedException("Invalid token")
        }
        catch(e: MalformedJwtException) {
            throw AccessDeniedException("Invalid token")
        }
    }

    fun isTokenValid(string: String): Boolean
    {
        if(string.isBlank())
        {
            return false
        }
        var isValid = false
        try
        {

            isValid = true
        }
        catch(e: SecurityException) {}
        catch(e: ExpiredJwtException) {}
        catch(e: MalformedJwtException) {}
        return isValid
    }

    fun getTokenUsername(string: String): String
    {
        var username = ""
        if (isTokenValid(string))
        {
            username = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(string)
                .body.subject
        }
        return username
    }
}
