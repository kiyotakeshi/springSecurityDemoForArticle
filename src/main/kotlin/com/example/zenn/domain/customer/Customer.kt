package com.example.zenn.domain.customer

import com.example.zenn.domain.role.Role

class Customer private constructor(
    id: Int?, email: String, password: String, roles: Set<Role>?
) {
    var id: Int? = id
    val email = email
    val password = password
    val roles: Set<Role>? = roles

    companion object {
        fun create(email: String, password: String, roles: Set<Role>?): Customer {
            return Customer(null, email, password, roles)
        }
        fun reconstruct(id: Int, email: String, password: String, roles: Set<Role>): Customer {
            return Customer(id, email, password, roles)
        }
    }

    override fun toString(): String {
        return "Customer(id=$id, email='$email', password='$password', roles=$roles)"
    }
}
