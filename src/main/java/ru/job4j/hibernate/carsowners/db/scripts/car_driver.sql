create table car_driver(
 id serial primary key,
 driver_id int not null references driver(id),
 car_id int not null references car(id)
);