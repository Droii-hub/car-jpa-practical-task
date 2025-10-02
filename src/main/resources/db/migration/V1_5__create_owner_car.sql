create table owner_car(
    fk_owner bigint not null references owner(id),
    fk_car bigint not null references car(id),
    unique (fk_owner, fk_car)
)