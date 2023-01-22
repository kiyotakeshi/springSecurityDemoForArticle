package com.example.zenn

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * @author kiyota
 */
@Repository
interface RoleRepository : CrudRepository<Role, Int> {
    fun findByName(name: String): Role?
    fun findByNameIn(names: List<String>): Set<Role>?
}
