package com.example.zenn.security

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.util.StringUtils
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * @author kiyota
 */
class AuthorizationHeaderValidationFilter :Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse
        // example: `Authorization: Basic YWRtaW5AZXhhbXBsZS5jb206MXFhenhzdzI=`
        var header = req.getHeader(HttpHeaders.AUTHORIZATION)
        if (header != null) {
            validate(header, res)
        }
        chain.doFilter(request, response)
    }

    private fun validate(header: String, res: HttpServletResponse) {
        // Basic YWRtaW5AZXhhbXBsZS5jb206MXFhenhzdzI=
        header.trim { it <= ' ' }
        if (StringUtils.startsWithIgnoreCase(header, "Basic")) {
            val base64Token = header.substring(6).toByteArray(StandardCharsets.UTF_8)
            try {
                // admin@example.com:1qazxsw2
                val token = String(Base64.getDecoder().decode(base64Token), StandardCharsets.UTF_8)
                val delim = token.indexOf(":")
                if (delim == -1) {
                    throw BadCredentialsException("Invalid basic authentication token")
                }
                val email = token.substring(0, delim)
                if (email.lowercase(Locale.getDefault()).contains("test")) {
                    res.status = HttpServletResponse.SC_BAD_REQUEST
                    return
                }
            } catch (e: IllegalArgumentException) {
                throw BadCredentialsException("Failed to decode basic authentication token")
            }
        }
    }
}