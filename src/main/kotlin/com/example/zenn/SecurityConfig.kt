package com.example.zenn

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager

/**
 * @author kiyota
 */
@Configuration
class SecurityConfig {

    @Bean
    fun userDetailsManager(): UserDetailsManager {
        val admin: UserDetails = User.builder()
            .username("admin")
            // encode with Spring Boot CLI
            // https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#authentication-password-storage-boot-cli
            // $ spring encodepassword 1qazxsw2
            .password("{bcrypt}\$2a\$10\$1gHHMqYmv7spE.896lYtKuenhXSRGyZ0FK.JTzAOSD6qgRKtPl5wy")
            .authorities("USER", "ADMIN")
            .build()
        val user: UserDetails = User.builder()
            .username("user")
            // $ spring encodepassword 2wsxzaq1
            .password("{bcrypt}\$2a\$10\$saAFPwyIghNePc0C4sKuUOBUIQBs6xnC8sUh2OvLW6fuU57oJ1tp6")
            .authorities("USER")
            .build()
        return InMemoryUserDetailsManager(admin, user)
    }
}