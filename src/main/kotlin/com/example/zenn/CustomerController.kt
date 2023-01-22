package com.example.zenn

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * @author kiyota
 */
@RestController
class CustomerController(
    private val customerRepository: CustomerRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @PostMapping("/register")
    fun registerUser(@RequestBody customer: Customer): ResponseEntity<*>? {
        var response: ResponseEntity<*>? = null
        try {
            val hashedPassword = passwordEncoder.encode(customer.getPassword())
            val newCustomer = Customer(customer.getEmail(), hashedPassword, customer.getRole())
            val savedCustomer = customerRepository.save(newCustomer)
            if (savedCustomer.getId() > 0) {
                response = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Given user details are successfully registered")
            }
        } catch (ex: Exception) {
            response = ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An exception occurred due to " + ex.message)
        }
        return response
    }
}
