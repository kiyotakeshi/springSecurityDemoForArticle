package com.example.zenn.domain.customer

/**
 * @author kiyota
 */
interface CustomerRepository {
    fun findByEmail(email: String): Customer?
    fun save(customer: Customer): Customer
    fun findAll(): List<Customer>
    fun attachRoles(customerId: Int, roles: List<String>): Customer
}