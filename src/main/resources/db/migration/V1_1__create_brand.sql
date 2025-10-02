create table brand(
    id bigserial primary key,
    name varchar(30) not null unique,
    created timestamp not null,
    updated timestamp
)