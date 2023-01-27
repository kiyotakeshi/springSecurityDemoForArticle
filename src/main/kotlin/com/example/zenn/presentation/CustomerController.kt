package com.example.zenn.presentation

import com.example.zenn.domain.customer.Customer
import com.example.zenn.security.CustomerDetails
import com.example.zenn.security.TokenService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * @author kiyota
 */
@RestController
class CustomerController(
    private val customerDetails: CustomerDetails,
    private val tokenService: TokenService
    ) {
    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(CustomerController::class.java)
    }
    @PostMapping("/register")
    fun registerUser(@RequestBody customer: Customer): ResponseEntity<*> {
        val response: ResponseEntity<*> = try {
            val savedCustomer: Customer = customerDetails.register(customer);
            ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Given user details are successfully registered $savedCustomer")
        } catch (ex: Exception) {
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An exception occurred due to " + ex.message)
        }
        return response
    }

    @GetMapping("/token")
    fun getUserDetailsAfterLogin(authentication: Authentication): String {
        LOG.debug("token requested for user: ${authentication.name}")
        val token = tokenService.generateToken(authentication)
        LOG.debug("token generated: $token")
        return token
    }
}
