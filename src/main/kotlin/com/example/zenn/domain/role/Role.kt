package com.example.zenn.domain.role

class Role private constructor(
    id: Int?, name: String
){
    var id: Int? = id
    val name = name

    companion object {
        fun create(name: String): Role {
            return Role(null, name)
        }
        fun reconstruct(id: Int, name: String): Role {
            return Role(id, name)
        }
    }

    override fun toString(): String {
        return "Role(id=$id, name='$name')"
    }
}
