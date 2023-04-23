create table if not exists _user(
	id serial not null primary key,
	username varchar(20) not null unique,
	password varchar(256) not null
);

create table if not exists company(
	id serial not null primary key,
	name varchar(128) not null unique,
	description text,
	password varchar(256) not null
);

create table if not exists device(
	id serial not null primary key,
	name varchar(50) not null,
	type varchar(128),
	description text,
	price int not null,
	company_id int not null references company(id),
	unique (name, company_id)
);

create table if not exists cart(
	id serial not null primary key,
	user_id int not null references _user(id) unique
);

create table if not exists devices_in_cart(
	id serial not null primary key,
	cart_id int references cart(id),
	device_id int references device(id),
	amount int not null default 1,
	unique (cart_id, device_id)
);

create table if not exists transaction(
	id serial not null primary key,
	customer_id int not null references _user(id),
	seller_id int not null references company(id),
	device_id int not null references device(id),
	amount int not null,
	status varchar(64),
	date_time timestamp default current_timestamp
);