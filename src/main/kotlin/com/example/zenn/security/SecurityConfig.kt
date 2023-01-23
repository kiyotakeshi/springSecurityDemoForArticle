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
                    .requestMatchers("/me").authenticated()
            }
            .formLogin()
            .and().httpBasic()
            // TODO: enable
            .and().csrf().disable()
            .addFilterBefore(AuthorizationHeaderValidationFilter(), BasicAuthenticationFilter::class.java)
            .addFilterBefore(JWTValidationFilter(), BasicAuthenticationFilter::class.java)
            .addFilterAfter(LoggingAuthoritiesFilter(), BasicAuthenticationFilter::class.java)
            .addFilterAfter(JWTGenerationFilter(), LoggingAuthoritiesFilter::class.java)
        return http.build()
    }

    // https://docs.spring.io/spring-security/reference/reactive/integrations/cors.html
//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val configuration = CorsConfiguration()
//        configuration.allowedOrigins = listOf("https://localhost:3000")
//        configuration.allowedMethods = listOf("GET", "POST")
//        configuration.allowCredentials = true
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", configuration)
//        return source
//    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}