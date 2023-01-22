package com.example.zenn

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * @author kiyota
 */
@Repository
open interface CustomerRepository : CrudRepository<Customer, Long> {
    fun findByEmail(email: String): List<Customer>
}
