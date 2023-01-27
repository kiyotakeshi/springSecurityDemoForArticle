package com.example.zenn.security

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


/**
 * @author kiyota
 */
@Configuration
class SecurityConfig(private val rsaKeyProperties: RsaKeyProperties) {

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
                    .requestMatchers("/token").authenticated()
            }
            .httpBasic()
            // TODO: enable
            .and().csrf().disable()
            .addFilterBefore(AuthorizationHeaderValidationFilter(), BasicAuthenticationFilter::class.java)
            .addFilterAfter(LoggingAuthoritiesFilter(), BasicAuthenticationFilter::class.java)
            .oauth2ResourceServer {
                it.jwt()
        }
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

    @Bean
    fun jwtDecoder(): JwtDecoder? {
        return NimbusJwtDecoder.withPublicKey(rsaKeyProperties.publicKey).build()
    }

    @Bean
    fun jwtEncoder(): JwtEncoder? {
        val jwk: JWK = RSAKey.Builder(rsaKeyProperties.publicKey).privateKey(rsaKeyProperties.privateKey).build()
        val jwks: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }

    /**
     * JWT を Authentication に変換する JwtAuthenticationConverter の挙動のカスタマイズ
     * `SCOPE_` の prefix がつくが、それを取り除く
     * [com.example.zenn.security.CustomerDetails#loadUserByUsername] にて `ROLE_` の prefix を付与しているため
     * see [Authorities Prefix Configuration](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html#oauth2resourceserver-jwt-authorization-extraction:~:text=Authorities%20Prefix%20Configuration)
     * see [fixed issue](https://github.com/spring-projects/spring-security/pull/7256)
     */
    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val grantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
        grantedAuthoritiesConverter.setAuthorityPrefix("")

        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter)
        return jwtAuthenticationConverter
    }
}