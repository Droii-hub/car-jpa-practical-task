create table model(
    id bigserial primary key,
    name varchar(30) not null,
    brand_id bigint not null references brand(id),
    unique(name, brand_id),
    created timestamp not null,
    updated timestamp
)