package com.example.zenn.security

import com.example.zenn.Customer
import com.example.zenn.CustomerRepository
import com.example.zenn.Role
import com.example.zenn.RoleRepository
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

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val customers = customerRepository.findByEmail(email)
        return if (customers.isEmpty()) {
            throw UsernameNotFoundException("User details not found for the user : $email")
        } else {
            val customer = customers[0]
            val authorities: MutableList<GrantedAuthority> = ArrayList()
            customer.getRoles().map {
                authorities.add(SimpleGrantedAuthority("ROLE_${it?.getName()}"))
            }
            User(customer.getEmail(), customer.getPassword(), authorities)
        }
    }

    fun register(customer: Customer): Customer {
        val hashedPassword = passwordEncoder.encode(customer.getPassword())
        val userRole: Role? = roleRepository.findByName("USER")
        val newCustomer = Customer(customer.getEmail(), hashedPassword, setOf(userRole))
        return customerRepository.save(newCustomer)
    }
}
