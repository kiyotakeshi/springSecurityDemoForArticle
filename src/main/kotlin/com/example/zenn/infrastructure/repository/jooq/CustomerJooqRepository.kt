package com.example.zenn.infrastructure.repository.jooq

import com.example.zenn.domain.customer.Customer
import com.example.zenn.domain.customer.CustomerRepository
import com.example.zenn.domain.role.Role
import com.example.zenn.jooq.codegen.tables.CustomerRole
import com.example.zenn.jooq.codegen.tables.Customers
import com.example.zenn.jooq.codegen.tables.Roles
import com.example.zenn.jooq.codegen.tables.records.CustomersRecord
import com.example.zenn.jooq.codegen.tables.references.CUSTOMERS
import com.example.zenn.jooq.codegen.tables.references.CUSTOMER_ROLE
import com.example.zenn.jooq.codegen.tables.references.ROLES
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Record1
import org.jooq.Result
import org.jooq.impl.DSL.exists
import org.jooq.impl.DSL.select
import org.jooq.impl.QOM.Exists
import org.springframework.stereotype.Repository

@Repository
class CustomerJooqRepository(private val create: DSLContext) : CustomerRepository {

    val c: Customers = CUSTOMERS.`as`("c")
    val cr: CustomerRole = CUSTOMER_ROLE.`as`("cr")
    val r: Roles = ROLES.`as`("r")

    override fun findByEmail(email: String): Customer? {

        val records = create.select()
            .from(c.leftOuterJoin(cr).on(cr.CUSTOMER_ID.eq(c.ID)))
            .leftOuterJoin(r).on(r.ID.eq(cr.ROLE_ID))
            .where(c.EMAIL.eq(email))
            .fetch()

        if (records.isEmpty()) {
            return null
        }

        val roles = records.map {
            Role.reconstruct(
                id = it.getValue(r.ID)!!,
                name = it.getValue(r.NAME)!!
            )
        }.toSet()

        val first: Record = records.first()

        return Customer.reconstruct(
            id = first.getValue(c.ID)!!,
            email = first.getValue(c.EMAIL)!!,
            password = first.getValue(c.PASSWORD)!!,
            roles = roles
        );
    }

    override fun save(customer: Customer): Customer {
        val returningCustomer: CustomersRecord = create.insertInto(c, c.EMAIL, c.PASSWORD)
            .values(customer.email, customer.password)
            .returning()
            .first()

        create.insertInto(cr, cr.CUSTOMER_ID, cr.ROLE_ID)
            .values(returningCustomer.getValue(c.ID), customer.roles?.first()?.id)
            .execute()

        return returningCustomer.map {
            Customer.reconstruct(
                it.getValue(c.ID)!!,
                it.getValue(c.EMAIL)!!,
                it.getValue(c.PASSWORD)!!,
                setOfNotNull(customer.roles?.first())
            )
        }
    }

    override fun findAll(): List<Customer> {
        val records = create.select(c.ID, c.EMAIL, c.PASSWORD, r.ID, r.NAME)
            .from(c.leftOuterJoin(cr).on(cr.CUSTOMER_ID.eq(c.ID)))
            .leftOuterJoin(r).on(r.ID.eq(cr.ROLE_ID))
            .fetch()
            .intoGroups(c.EMAIL)

        val customers = mutableListOf<Customer>()

        records.values.map {
            val roles = it.map {
                Role.reconstruct(
                    id = it.getValue(r.ID)!!,
                    name = it.getValue(r.NAME)!!
                )
            }.toSet()

            val reconstruct: Customer = Customer.reconstruct(
                id = it.first().getValue(c.ID)!!,
                email = it.first().getValue(c.EMAIL)!!,
                password = it.first().getValue(c.PASSWORD)!!,
                roles = roles
            )
            customers.add(reconstruct)
        }
        return customers
    }

    override fun attachRoles(customerId: Int, roles: List<String>): Customer {
        val customer = create.select()
            .from(c)
            .where(c.ID.eq(customerId))
            .first()

        val existsRoles = create.select()
            .from(r)
            .where(r.NAME.`in`(roles))
            .fetch()

        existsRoles.map {
            create.insertInto(cr, cr.CUSTOMER_ID, cr.ROLE_ID)
                .values(customer.getValue(c.ID), it.getValue(r.ID))
                .execute()
        }

        return findByEmail(customer.getValue(c.EMAIL)!!)!!
    }
}