package com.example.zenn

import jakarta.persistence.*

/**
 * @author kiyota
 *
 *
 * 事前に以下の DDL を流しておく必要あり(最終的には flyway で流す)
drop table if exists customers;
drop sequence if exists customer_id_seq;

create sequence customer_id_seq;
create table customers
(
id       int          not null default nextval('customer_id_seq') primary key,
email    varchar(50)  not null unique,
password varchar(500) not null
-- role varchar(50) not null
);

drop table if exists roles;
drop sequence if exists role_id_seq;

create sequence role_id_seq;
create table roles
(
id   int         not null default nextval('role_id_seq') primary key,
name varchar(50) not null unique
);

drop table if exists customer_role;
drop sequence if exists customer_role_id_seq;

create sequence customer_role_id_seq;
create table customer_role
(
id          int not null default nextval('customer_role_id_seq') primary key,
customer_id int not null,
role_id     int not null,
constraint fk_customer_role_customer_id foreign key (customer_id) references customers (id),
constraint fk_customer_role_role_id foreign key (role_id) references roles (id)
);

-- 登録したデータを取得する場合
select c.*, r.name
from customers c
left join customer_role cr on c.id = cr.customer_id
left join roles r on cr.role_id = r.id;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "customer_role",
        joinColumns = [JoinColumn(name = "customer_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    private var roles: Set<Role?> = emptySet()

    fun getId(): Int {
        return id
    }

    fun getEmail(): String? {
        return email
    }

    fun getPassword(): String? {
        return password
    }

    fun getRoles(): Set<Role?> {
        return roles
    }

    protected constructor()

    constructor(email: String?, password: String?, roles: Set<Role?>) {
        this.email = email
        this.password = password
        this.roles = roles
    }

    fun addRoles(roles :Set<Role>) {
        this.roles.plus(roles)
    }
}
