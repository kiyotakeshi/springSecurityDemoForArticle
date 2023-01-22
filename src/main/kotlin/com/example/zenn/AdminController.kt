package com.example.zenn

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * @author kiyota
 */
@RestController
class AdminController(
    private val customerRepository: CustomerRepository,
    private val roleRepository: RoleRepository,
) {
    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roles")
    fun addRole(@RequestBody role: Role): Role = roleRepository.save(role)

    @GetMapping("/roles")
    fun getRoles(): List<Role> = roleRepository.findAll().toList()

    @GetMapping("/customers")
    fun getCustomers(): List<Customer> = customerRepository.findAll().toList()

    @PutMapping("/customers/{customerId}/roles")
    fun attachRoles(@PathVariable customerId: Int, @RequestBody roles: List<String>) {
    }
}