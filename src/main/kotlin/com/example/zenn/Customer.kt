package com.example.zenn

import jakarta.persistence.*

/**
 * @author kiyota
 */
@Entity
@Table(name = "customers")
class Customer {

    // DB の layer で sequence を設定済
    // -- create sequence customer_id_seq;
    // DDL で sequence を使うことを指定しているので、
    // -- create table customers
    // -- (
    // --     id       integer default nextval('customer_id_seq'::regclass) not null
    // --         primary key,
    // --     email    varchar(50)                                          not null
    // --         unique,
    // --     password varchar(500)                                         not null,
    // --     role     varchar(50)                                          not null
    // -- );
    // 以下の書き方でも良い
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_generator")
    @SequenceGenerator(name = "customer_generator", sequenceName = "customer_id_seq", allocationSize = 1)
    private val id = 0
    private var email: String? = null
    private var password: String? = null
    private var role: String? = null
    fun getId(): Int {
        return id
    }

    fun getEmail(): String? {
        return email
    }

    fun getPassword(): String? {
        return password
    }

    fun getRole(): String? {
        return role
    }

    protected constructor()

    constructor(email: String?, password: String?, role: String?) {
        this.email = email
        this.password = password
        this.role = role
    }
}
