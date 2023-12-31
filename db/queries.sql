SELECT
    userroles.user_id as user_id,
    roles.*
from userroles
inner join roles on userroles.role_id = roles.id
WHERE userroles.user_id = 3;

select * from users where id = 3;