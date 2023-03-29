create table users(
user_id integer primary key not null,
user_name varchar(50) not null,
email varchar(50),
password text not null
);

create table recipes(
recipe_id integer primary key not null,
user_id integer not null,
recipe_name varchar(50) not null,
created_date_time timestamp,
is_vegetarian boolean not null,
number_of_people integer,
ingredients varchar(100) not null,
cooking_instructions varchar(200) not null
);
alter table recipes add constraint recipe_user_fk
foreign key (user_id) references users(user_id);

create sequence users_seq;
create sequence recipes_seq;
