create table if not exists item(
id serial primary key,
descriptions text,
created timestamp,
user_id int not null references j_user(id),
done boolean
);