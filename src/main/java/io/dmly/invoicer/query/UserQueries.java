package io.dmly.invoicer.query;

public class UserQueries {
    public static final String GET_EMAILS_COUNT_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String INSERT_USER_QUERY = "INSERT INTO Users(first_name, last_name, email, password) " +
            "VALUES (:firstName, :lastName, :email, :password)";
    public static final String INSERT_ACCOUNT_VERIFICATION_URL_QUERY = "INSERT INTO AccountVerifications(user_id, url) " +
            "VALUES(:userId, :url)";
    public static final String INSERT_SET_ROLE_FOR_USER_QUERY = "INSERT INTO UserRoles(user_id, role_id) " +
            "VALUES(:userId, :roleId)";
    public static final String SELECT_USER_BY_EMAIL_QUERY = """
            SELECT Users.*, Roles.name role_name, Roles.permission, Roles.id role_id
            FROM Users
            LEFT JOIN UserRoles ON Users.id = UserRoles.user_id
            LEFT JOIN Roles ON UserRoles.role_id = Roles.id
            WHERE Users.email = :email
            """;
    public static final String UPSERT_VERIFICATION_CODE_FOR_USER_QUERY = """
            INSERT INTO TwoFactorVerifications (user_id, code, expiration_date) 
            VALUES(:userId, :code, TO_TIMESTAMP(:expirationDate, 'YYYY-MM-DD HH24:MI:SS')) 
            ON CONFLICT(user_id) DO 
            UPDATE SET code=:code, expiration_date=TO_TIMESTAMP(:expirationDate, 'YYYY-MM-DD HH24:MI:SS')
            """;
    public static final String SELECT_USER_BY_EMAIL_AND_VALID_CODE_QUERY = """
            SELECT Users.*
            FROM Users
                     INNER JOIN TwoFactorVerifications as codes on users.id = codes.user_id
            WHERE users.email = :email
              AND codes.code = :code
              AND codes.expiration_date >= now();
            """;
    public static final String DELETE_VERIFICATION_CODE_BY_USER_ID_QUERY = "DELETE FROM TwoFactorVerifications WHERE user_id = :userId";
    public static final String UPSERT_RESET_PASSWORD_VERIFICATION_FOR_USER_QUERY = """
            INSERT INTO ResetPasswordVerifications (user_id, key, expiration_date)
            VALUES(:userId, :key, TO_TIMESTAMP(:expirationDate, 'YYYY-MM-DD HH24:MI:SS'))
            ON CONFLICT(user_id) DO
            UPDATE SET key=:key, expiration_date=TO_TIMESTAMP(:expirationDate, 'YYYY-MM-DD HH24:MI:SS')
            """;
    public static final String SELECT_RESET_PASSWORD_VERIFICATION_ENTITY_QUERY = """
            SELECT * FROM ResetPasswordVerifications
            WHERE key = :key
            """;
    public static final String SELECT_USER_BY_ID_QUERY = """
            SELECT Users.*, Roles.name role_name, Roles.permission, Roles.id role_id
            FROM Users
            LEFT JOIN UserRoles ON Users.id = UserRoles.user_id
            LEFT JOIN Roles ON UserRoles.role_id = Roles.id 
            WHERE Users.id = :id
            """;
    public static final String UPDATE_USER_PASSWORD_BY_RESET_PASSWORD_KEY_QUERY = """
            UPDATE Users SET password = :password 
            WHERE id = (SELECT user_id FROM ResetPasswordVerifications WHERE key = :key)
            """;
    public static final String DELETE_RESET_PASSWORD_REQUEST_ENTITY_BY_KEY_QUERY = """
            DELETE FROM ResetPasswordVerifications WHERE key = :key
            """;
    public static final String SET_USER_ENABLED_QUERY = """
            UPDATE Users SET enabled = true, non_locked = true WHERE id = :id
            """;
    public static final String UPDATE_USER_DETAILS_QUERY = """
            UPDATE Users 
            SET 
                first_name = :firstName, last_name = :lastName, email = :email,
                phone = :phone, address = :address, title = :title,
                bio = :bio, image_url = :imageUrl
            WHERE
                id = :id
            """;
}
