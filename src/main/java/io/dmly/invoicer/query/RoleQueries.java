package io.dmly.invoicer.query;

public class RoleQueries {
    public static final String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM Roles WHERE name = :roleName";
    public static final String SELECT_ROLE_BY_USER_ID = """
            SELECT
                userroles.user_id as user_id,
                roles.*
            from userroles
            inner join roles on userroles.role_id = roles.id
            WHERE userroles.user_id = :userId""";

    public static final String SELECT_ROLE_BY_USER_EMAIL = """
            SELECT
                users.id as user_id,
                userroles.role_id,
                roles.*
            from users
            inner join userroles on users.id = userroles.user_id
            inner join roles on userroles.role_id = roles.id
            WHERE users.email = :email""";
}
