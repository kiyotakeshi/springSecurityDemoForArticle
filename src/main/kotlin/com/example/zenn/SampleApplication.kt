package com.example.zenn

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity(debug = true)
class SampleApplication

fun main(args: Array<String>) {
	runApplication<SampleApplication>(*args)
}
