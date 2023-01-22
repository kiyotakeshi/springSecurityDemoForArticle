package com.example.zenn.security

import com.example.zenn.domain.customer.Customer
import com.example.zenn.domain.customer.CustomerRepository
import com.example.zenn.domain.role.Role
import com.example.zenn.domain.role.RoleRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * @author kiyota
 */
@Service
class CustomerDetails(
    private val customerRepository: CustomerRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val customer = customerRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User details not found for the user : $email")

        val authorities: MutableList<GrantedAuthority> = ArrayList()
        customer.roles?.map {
            authorities.add(SimpleGrantedAuthority("ROLE_${it.name}"))
        }
        return User(customer.email, customer.password, authorities)
    }

    fun register(customer: Customer): Customer {
        val hashedPassword = passwordEncoder.encode(customer.password)
        val userRole: Role? = roleRepository.findByName("USER")
        val newCustomer = Customer.create(customer.email, hashedPassword, setOfNotNull(userRole))
        return customerRepository.save(newCustomer)
    }
}
