create table if not exists USERS(
	USER_ID SERIAL primary key,
	email varchar(100) not null,
	username varchar(100) not null,
	password varchar(100) not null,
	description varchar(100),
	created_at timestamp not null,
	updated_at timestamp,
	deleted_at timestamp
);
create table if not exists TASK(
	TASK_ID SERIAL PRIMARY KEY,
	description varchar(100),
	owner varchar(100),
	created_at timestamp not null,
	updated_at timestamp,
	deleted_at timestamp,
	USER_ID int,
	FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);