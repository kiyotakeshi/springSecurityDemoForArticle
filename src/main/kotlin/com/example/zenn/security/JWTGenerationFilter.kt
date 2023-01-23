package com.example.zenn.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

class JWTGenerationFilter : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (null != authentication) {
            val jwt = generateJWT(authentication)
            response.setHeader(HttpHeaders.AUTHORIZATION, jwt)
        }
        filterChain.doFilter(request, response)
    }

    /**
     * only exec filter `/me`
     */
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath != "/me"
    }

    /**
     * generate following JWT
     * **see:** [sample JWT](https://jwt.io/#debugger-io?token=eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTcHJpbmcgU2VjdXJpdHkgU2FtcGxlIiwic3ViIjoiSldUIiwidXNlcm5hbWUiOiJtaWtlQGV4YW1wbGUuY29tIiwicm9sZXMiOlsiUk9MRV9URVNUIiwiUk9MRV9VU0VSIl0sImlhdCI6MTY3NDQ4NTMzOSwiZXhwIjoxNjc0NTE1MzM5fQ.polgUWFjc4g9de22YVcVKYDB6xoF5-In6xLibkB5toA)
     */
    private fun generateJWT(authentication: Authentication): String {
        val key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.toByteArray(StandardCharsets.UTF_8))
        val now = Date()
        val eightHoursAfter = Date(now.time + (1000 * 60 * 60 * 8))
        return Jwts.builder()
            .setIssuer("Spring Security Sample")
            .setSubject("JWT")
            .claim("username", authentication.name)
            .claim("roles", authentication.authorities
                .map { it.authority }
                .toSet()
            )
            .setIssuedAt(now)
            .setExpiration(eightHoursAfter)
            .signWith(key)
            .compact()
    }
}
