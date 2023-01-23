package com.example.zenn

import com.example.zenn.domain.customer.Customer
import com.example.zenn.security.CustomerDetails
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author kiyota
 */
@RestController
class CustomerController(
    private val customerDetails: CustomerDetails,
) {
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

    @RequestMapping("/me")
    fun getUserDetailsAfterLogin(authentication: Authentication): Customer =
        customerDetails.findByEmail(authentication.name)
}
