package com.example.zenn

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import javax.sql.DataSource

/**
 * @author kiyota
 */
@Configuration
class SecurityConfig {

    // exec following DDL in advance
    // custom for Postgres
    // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/jdbc.html#servlet-authentication-jdbc-schema-user
    // `org/springframework/security/core/userdetails/jdbc/users.ddl`
    /*
        drop table if exists authorities;
        drop table if exists users;

        drop sequence if exists user_id_seq;
        drop sequence if exists authority_id_seq;

        create sequence user_id_seq;
        create sequence authority_id_seq;

        create table users
        (
            id int not null default nextval('user_id_seq') primary key,
            username varchar(50)  not null unique,
            password varchar(500) not null,
            enabled  boolean                 not null
        );

        create table authorities
        (
            id int not null default nextval('authority_id_seq') primary key,
            username  varchar(50) not null,
            authority varchar(50) not null,
            constraint fk_authorities_users foreign key (username) references users (username)
        );

        create unique index ix_auth_username on authorities (username, authority);
     */
    @Bean
    fun users(dataSource: DataSource): UserDetailsManager {
        // create sample user
        /*
        val user: UserDetails = User.builder()
            .username("admin")
            // encode with Spring Boot CLI
            // https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#authentication-password-storage-boot-cli
            // $ spring encodepassword 1qazxsw2
            .password("$2a\$10\$1gHHMqYmv7spE.896lYtKuenhXSRGyZ0FK.JTzAOSD6qgRKtPl5wy")
            .authorities("USER", "ADMIN")
            .build()
        val admin: UserDetails = User.builder()
            .username("user")
            // $ spring encodepassword 2wsxzaq1
            .password("$2a\$10\$saAFPwyIghNePc0C4sKuUOBUIQBs6xnC8sUh2OvLW6fuU57oJ1tp6")
            .authorities("USER")
            .build()

        val users = JdbcUserDetailsManager(dataSource)
        users.createUser(user)
        users.createUser(admin)
        return users
         */
        return JdbcUserDetailsManager(dataSource)
    }
}