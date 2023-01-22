package com.example.zenn.infrastructure.repository.jooq

import com.example.zenn.domain.role.Role
import com.example.zenn.domain.role.RoleRepository
import com.example.zenn.jooq.codegen.tables.CustomerRole
import com.example.zenn.jooq.codegen.tables.Roles
import com.example.zenn.jooq.codegen.tables.references.CUSTOMER_ROLE
import com.example.zenn.jooq.codegen.tables.references.ROLES
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class RoleJooqRepository(private val create:DSLContext) : RoleRepository {

    val r: Roles = ROLES.`as`("r")

    override fun findByName(name: String): Role? {

        val records = create.select()
            .from(r)
            .where(r.NAME.eq(name))
            .fetch()

        if(records.isEmpty()){
            return null
        }

        val first: Record = records.first()

        return Role.reconstruct(
            id = first.getValue(r.ID)!!,
            name = first.getValue(r.NAME)!!
        )
    }

    override fun save(role: Role): Role {
        val returningRole = create.insertInto(r, r.NAME)
            .values(role.name)
            .returning()
            .first()

        return returningRole.map {
            Role.reconstruct(
                it.getValue(r.ID)!!,
                it.getValue(r.NAME)!!
            )
        }
    }

    override fun findAll(): List<Role> {
        val records = create.select()
            .from(r)
            .fetch()

        return records.map {
            Role.reconstruct(
                id = it.getValue(r.ID)!!,
                name = it.getValue(r.NAME)!!
            )
        }.toList()
    }
}