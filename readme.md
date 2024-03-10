# ToDo

## Planning API

https://blogs.mulesoft.com/dev-guides/api-design/api-best-practices-series-plan/


## SQL
```roomsql
DELETE FROM TASK;
DELETE FROM USERS;

ALTER SEQUENCE users_user_id_seq RESTART WITH 1;
ALTER SEQUENCE task_task_id_seq RESTART WITH 1;
-- test/resources
--ALTER TABLE TASK ALTER COLUMN TASK_ID RESTART WITH 1;
--ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;

INSERT INTO USERS(DESCRIPTION,USERNAME,EMAIL,PASSWORD,CREATED_AT,UPDATED_AT,DELETED_AT)
VALUES
    ('John Doe','qqq','qqq@gmail.com','secure','2023-10-27T12:30:00',null,null),
    ('N N','aaa','aaa@gmail.com','secure','2023-10-27T12:30:00',null,null),
    ('Name Surname','zzz','zzz@gmail.com','secure','2023-10-27T12:30:00',null,null);

INSERT INTO TASK (DESCRIPTION,OWNER,CREATED_AT,UPDATED_AT,DELETED_AT,USER_ID)
VALUES
    ('API','qqq','2023-10-27T12:30:00',null,null,1),
    ('REACT CLONE','qqq','2023-10-27T12:30:00',null,null,1),
    ('Homework','aaa','2023-10-27T12:30:00',null,null,2),
    ('shopping','qqq','2023-10-27T12:30:00',null,null,1),
    ('stuff','aaa','2023-10-27T12:30:00',null,null,2),
    ('random','qqq','2023-10-27T12:30:00',null,null,1),
    ('Work','zzz','2023-10-27T12:30:00',null,null,3),
    ('Angular','zzz','2023-10-27T12:30:00',null,null,3),
    ('Java','zzz','2023-10-27T12:30:00',null,null,3),
    ('Spring','aaa','2023-10-27T12:30:00',null,null,2),
    ('PostgreSQL','qqq','2023-10-27T12:30:00',null,null,1),
    ('Scala','aaa','2023-10-27T12:30:00',null,null,2),
    ('Python','aaa','2023-10-27T12:30:00',null,null,2);
```

## CONFIGURATION: Application.properties, Docker

```text
Development process consisted of iterative steps in which I made changes to application.yml files multiple times.
Firstly the app's db as well as the tests were running on H2.
Then the configuration was changed in the way so:
- the app db was moved to PostgreSQL.
- the tests remained the same
In the end I wrote SQL initialization scripts and introduced Migrations using Flyway. Also testcontainers were added.
Current configuration is:
- the app's db - PostgreSQL
- integration tests - testcontainers
- unit tests - H2
```

### Running docker container
```text
docker run -d --name postgresdb -p 5432:5432 -e POSTGRES_PASSWORD=... postgres
docker exec -it postgresdb bash
# psql -U postgres
CREATE DATABASE todo;

postgres=# \l
                                                List of databases
   Name    |  Owner   | Encoding |  Collate   |   Ctype    | ICU Locale | Locale Provider |   Access privileges
-----------+----------+----------+------------+------------+------------+-----------------+-----------------------
 postgres  | postgres | UTF8     | en_US.utf8 | en_US.utf8 |            | libc            |
 template0 | postgres | UTF8     | en_US.utf8 | en_US.utf8 |            | libc            | =c/postgres          +
           |          |          |            |            |            |                 | postgres=CTc/postgres
 template1 | postgres | UTF8     | en_US.utf8 | en_US.utf8 |            | libc            | =c/postgres          +
           |          |          |            |            |            |                 | postgres=CTc/postgres
 todo      | postgres | UTF8     | en_US.utf8 | en_US.utf8 |            | libc            |
 
postgres=# \c todo
You are now connected to database "todo" as user "postgres".

todo=# \dt
Did not find any relations.
```




## FOR LATER - Contracts

### /tasks

```text
Request
  URI: /tasks
  HTTP Verb: GET
  Body: None

Response:
  HTTP Status:
    200 OK if the user is authorized and the Tasks were successfully retrieved
    403 UNAUTHORIZED if the user is unauthenticated or unauthorized
    404 NOT FOUND if the user is authenticated and authorized but the Tasks cannot be found
  Response Body Type: JSON
  Example Response Body:
    {
      "id": 99,
      "amount": 123.45
    }
    
Request
  URI: /tasks
  HTTP Verb: POST
  Body: Task to be saved
  Example Request Body:
    {
      "description": "New task"
    }
Response:
  HTTP Status:
    201 CREATED if the user is authorized and the Task was successfully saved Location=/tasks/{createdId}
  Response Body Type: JSON
  Example Response Body:
    {
      "id": 99,
      "amount": 123.45
    }
```

### /tasks/{id}

```text
Request
  URI: /tasks/{id}
  HTTP Verb: GET
  Body: None

Response:
  HTTP Status:
    200 OK if the user is authorized and the Task was successfully retrieved
    403 UNAUTHORIZED if the user is unauthenticated or unauthorized
    404 NOT FOUND if the user is authenticated and authorized but the Task cannot be found
  Response Body Type: JSON
  Example Response Body:
    {
      "id": 99,
      "description": "Very long task."
    }
```