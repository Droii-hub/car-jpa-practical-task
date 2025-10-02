create table car (
    id bigserial primary key,
    creation_year int not null,
    number varchar(10) not null,
    color varchar(100) not null,
    model_id bigint not null references model(id),
    actualTechnicalInspection boolean,
    created timestamp not null,
    updated timestamp
);