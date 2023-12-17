select *
from users;
select *
from roles;

insert into users (first_name, last_name, email, phone)
VALUES ('John', 'Smith', 'test@test.com', '+1234567989');

insert into users (first_name, last_name, email, phone)
VALUES ('Jane', 'Dow', 'test_256@test.com', '+1234567989585');

insert into roles(name, permissions)
VALUES ('admin', 'read,write,delete,update');

insert into roles(name, permissions)
VALUES ('read', 'read');

insert into roles(name, permissions)
VALUES ('write', 'write');

insert into userroles(user_id, role_id)
VALUES (1, 1);

insert into userroles(user_id, role_id)
values (2, 2), (2, 3);



select *
from userroles;

select users.first_name, users.last_name, roles.name as role, roles.permissions
from users
         left join userroles u on users.id = u.user_id
         left join roles on u.role_id = roles.id
;

TRUNCATE TABLE userroles;
TRUNCATE TABLE roles CASCADE;
TRUNCATE TABLE users CASCADE;
