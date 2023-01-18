package com.example.zenn

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import kotlin.reflect.typeOf

@SpringBootApplication
@EnableWebSecurity(debug = true)
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
