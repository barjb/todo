create table if not exists GROUPS(
	GROUP_ID SERIAL primary key,
	description varchar(100),
	created_at timestamp not null,
	updated_at timestamp,
	deleted_at timestamp,
    USER_ID int,
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);

INSERT INTO GROUPS(DESCRIPTION,CREATED_AT,UPDATED_AT,DELETED_AT,USER_ID)
VALUES
    ('First ever group','2023-10-27T12:30:00',null,null,1),
    ('F1 fans','2023-10-27T12:30:00',null,null,2),
    ('Java user grop','2023-10-27T12:30:00',null,null,2);

create table if not exists USERS_GROUPS(
    USER_ID int,
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID),
    GROUP_ID int,
    FOREIGN KEY (GROUP_ID) REFERENCES GROUPS(GROUP_ID)
);

INSERT INTO USERS_GROUPS(USER_ID, GROUP_ID)
VALUES
    (1,1),
    (2,1),
    (3,1),
    (2,2),
    (2,3),
    (3,3);