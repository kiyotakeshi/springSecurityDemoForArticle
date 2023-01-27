package com.example.zenn

import com.example.zenn.security.RsaKeyProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableConfigurationProperties(RsaKeyProperties::class)
@SpringBootApplication
@EnableWebSecurity(debug = true)
// @EnableMethodSecurity
class SampleApplication

fun main(args: Array<String>) {
    val runApplication: ConfigurableApplicationContext = runApplication<SampleApplication>(*args)

    /*
    runApplication.beanDefinitionNames
        // .filter { it.contains("security") }
        // .filter { it.contains("defaultSecurityFilterChain") }
        .forEach(::println)
    */

    /*
    val bean = runApplication.getBean("defaultSecurityFilterChain")
    println(bean.javaClass.name) // org.springframework.security.web.DefaultSecurityFilterChain
    */
}
