create table if not exists item(
id serial primary key,
descriptions text,
created timestamp,
done boolean
);