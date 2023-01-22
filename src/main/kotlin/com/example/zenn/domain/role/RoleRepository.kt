package com.example.zenn.domain.role

/**
 * @author kiyota
 */
interface RoleRepository {
    fun findByName(name: String): Role?
    fun save(role: Role): Role
    fun findAll(): List<Role>
}