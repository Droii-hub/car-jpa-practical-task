create table owner (
    id bigserial primary key,
    email VARCHAR(255) not null UNIQUE CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'),
    password VARCHAR(255) NOT NULL,
    created timestamp not null,
    updated timestamp
);