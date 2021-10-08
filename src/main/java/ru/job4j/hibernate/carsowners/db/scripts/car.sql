create table car(
 id serial primary key,
 name varchar(255),
 engine_id int not null unique references engine(id)
);