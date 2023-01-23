package com.example.zenn.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


/**
 * @author kiyota
 */
@Configuration
class SecurityConfig {

    // spring-boot-autoconfigure-3.0.1.jar!/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
    // org/springframework/boot/autoconfigure/security/servlet/SpringBootWebSecurityConfiguration.java
    @Bean
    @Throws(Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeHttpRequests {
                it.requestMatchers("/register", "/public").permitAll()
                    .requestMatchers("/private").hasAnyRole("ADMIN", "USER")
                    .requestMatchers("/roles", "/customers/**").hasRole("ADMIN")
                    // .anyRequest().authenticated()
            }
            .formLogin()
            .and().httpBasic()
            // TODO: enable
            .and().csrf().disable()
            .addFilterBefore(AuthorizationHeaderValidationFilter(), BasicAuthenticationFilter::class.java)
            .addFilterAfter(LoggingAuthoritiesFilter(), BasicAuthenticationFilter::class.java)
        return http.build()
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}