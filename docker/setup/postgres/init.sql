drop table if exists customer_role;
drop sequence if exists customer_role_id_seq;

drop table if exists customers;
drop sequence if exists customer_id_seq;

create sequence customer_id_seq;
create table customers
(
    id       int          not null default nextval('customer_id_seq') primary key,
    email    varchar(50)  not null unique,
    password varchar(500) not null
);

drop table if exists roles;
drop sequence if exists role_id_seq;

create sequence role_id_seq;
create table roles
(
    id   int         not null default nextval('role_id_seq') primary key,
    name varchar(50) not null unique
);

create sequence customer_role_id_seq;
create table customer_role
(
    id          int not null default nextval('customer_role_id_seq') primary key,
    customer_id int not null,
    role_id     int not null,
    constraint fk_customer_role_customer_id foreign key (customer_id) references customers (id),
    constraint fk_customer_role_role_id foreign key (role_id) references roles (id)
);

-- initial data for admin user
insert into roles (id, name)
values (default, 'ADMIN'),
       (default, 'USER');

insert into customers (id, email, password)
values (default, 'admin@example.com', '$2a$10$ancDG4fEZY31a9OtuqSbs./SPUu7s00qam5sXinI5NrTLSGlCy/BK');

insert into customer_role (customer_id, role_id)
values ((select c.id from customers c where c.email = 'admin@example.com'),
        (select r.id from roles r where r.name = 'ADMIN')),
       ((select c.id from customers c where c.email = 'admin@example.com'),
        (select r.id from roles r where r.name = 'USER'));

select c.*, r.name
from customers c
         left join customer_role cr on c.id = cr.customer_id
         left join roles r on cr.role_id = r.id;
