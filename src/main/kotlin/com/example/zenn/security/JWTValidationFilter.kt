package com.example.zenn.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.nio.charset.StandardCharsets

class JWTValidationFilter : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val header = req.getHeader(HttpHeaders.AUTHORIZATION)
        // Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTcHJpbmcgU2VjdXJpdHkgU2FtcGxlIiwic3ViIjoiSldUIiwidXNlcm5hbWUiOiJtaWtlQGV4YW1wbGUuY29tIiwicm9sZXMiOlsiUk9MRV9URVNUIiwiUk9MRV9VU0VSIl0sImlhdCI6MTY3NDQ4NjUzNSwiZXhwIjoxNjc0NTE2NTM1fQ.IK4T9hIDikFQuI4hQIhm_z4sih_yYK7GEtPO9nwDEmE
        if (null != header) {
            if (StringUtils.startsWithIgnoreCase(header, "Bearer")) {
                val base64Token = header.substring(7)
                try {
                    val key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.toByteArray((StandardCharsets.UTF_8)))
                    val claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(base64Token)
                        .body

                    val roles = claims["roles"] as ArrayList<*>
                    val auth: Authentication = UsernamePasswordAuthenticationToken(
                        claims["username"].toString(),
                        null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(roles.joinToString(", "))
                    )
                    SecurityContextHolder.getContext().authentication = auth
                }
                catch (e: ExpiredJwtException) {
                    throw BadCredentialsException("Token is expired!\n${e.printStackTrace()}")
                }
                catch (e: Exception) {
                    throw BadCredentialsException("Invalid Token received!\n${e.printStackTrace()}")
                }
            }
        }
        filterChain.doFilter(req, res)
    }

    /**
     * only not exec filter `/me`
     */
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath == "/me"
    }

//    @JvmStatic
//    fun main(args: Array<String>) {
//        val yourList = List.of("hello", "world")
//        yourVarargMethod(*yourList.toTypedArray())
//    }
//
//    fun yourVarargMethod(vararg args: String?) {
//        for (str in args) {
//            println(str)
//        }
//    }
}
