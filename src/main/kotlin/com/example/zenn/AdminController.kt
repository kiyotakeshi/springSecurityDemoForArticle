package com.example.zenn

import com.example.zenn.domain.customer.Customer
import com.example.zenn.domain.customer.CustomerRepository
import com.example.zenn.domain.role.Role
import com.example.zenn.domain.role.RoleRepository
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * @author kiyota
 */
@RestController
class AdminController(
    // TODO: usecase layer をもうけてエラーハンドリング
    private val customerRepository: CustomerRepository,
    private val roleRepository: RoleRepository,
) {
    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roles")
    fun addRole(@RequestBody role: Role): Role = roleRepository.save(role)

    @GetMapping("/roles")
    fun getRoles(): List<Role> = roleRepository.findAll()

    @GetMapping("/customers")
    fun getCustomers(): List<Customer> = customerRepository.findAll()

    @PutMapping("/customers/{customerId}/roles")
    fun attachRoles(@PathVariable customerId: Int, @RequestBody roles: List<String>): Customer {
        return customerRepository.attachRoles(customerId, roles)
    }
}