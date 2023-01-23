package com.example.zenn.security

import jakarta.servlet.*
import org.springframework.security.core.context.SecurityContextHolder
import java.io.IOException
import java.util.logging.Logger

class LoggingAuthoritiesFilter : Filter {

    companion object {
        private val LOGGER = Logger.getLogger(LoggingAuthoritiesFilter::class.java.name)
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (null != authentication) {
            LOGGER.info(
                "User \"${authentication.name}\" is successfully authenticated and has the authorities ${authentication.authorities}"
            )
        }
        chain.doFilter(request, response)
    }

}
