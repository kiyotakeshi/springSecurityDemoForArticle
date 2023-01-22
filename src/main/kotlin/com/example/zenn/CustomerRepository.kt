package com.example.zenn

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * @author kiyota
 */
@Repository
interface CustomerRepository : CrudRepository<Customer, Int> {
    fun findByEmail(email: String): List<Customer>
}
