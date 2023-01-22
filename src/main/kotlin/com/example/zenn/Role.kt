package com.example.zenn

import jakarta.persistence.*


/**
 * @author kiyota
 */
@Entity
@Table(
    name = "roles",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name"])]
)
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_generator")
    @SequenceGenerator(name = "role_generator", sequenceName = "role_id_seq", allocationSize = 1)
    private val id = 0
    private var name: String? = null

    fun getId(): Int {
        return id
    }

    fun getName(): String? {
        return name
    }

    constructor(name: String?) {
        this.name = name
    }
    protected constructor()
}