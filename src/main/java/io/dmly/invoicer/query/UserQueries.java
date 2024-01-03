package io.dmly.invoicer.query;

public class UserQueries {
    public static final String GET_EMAILS_COUNT_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String INSERT_USER_QUERY = "INSERT INTO Users(first_name, last_name, email, password) " +
            "VALUES (:firstName, :lastName, :email, :password)";
    public static final String INSERT_ACCOUNT_VERIFICATION_URL = "INSERT INTO AccountVerifications(user_id, url) " +
            "VALUES(:userId, :url)";
    public static final String INSERT_SET_ROLE_FOR_USER = "INSERT INTO UserRoles(user_id, role_id) " +
            "VALUES(:userId, :roleId)";
    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM Users WHERE email = :email";
    public static final String UPSERT_VERIFICATION_CODE_FOR_USER = """
            INSERT INTO TwoFactorVerifications (user_id, code, expiration_date) 
            VALUES(:userId, :code, TO_TIMESTAMP(:expirationDate, 'YYYY-MM-DD HH:MI:SS')) 
            ON CONFLICT(user_id) DO 
            UPDATE SET code=:code, expiration_date=TO_TIMESTAMP(:expirationDate, 'YYYY-MM-DD HH:MI:SS')
            """;
    public static final String SELECT_USER_BY_EMAIL_AND_VALID_CODE = """
            SELECT Users.*
            FROM Users
                     INNER JOIN TwoFactorVerifications as codes on users.id = codes.user_id
            WHERE users.email = :email
              AND codes.code = :code
              AND codes.expiration_date >= now();
            """;
    public static final String DELETE_VERIFICATION_CODE_BY_USER_ID = "DELETE FROM TwoFactorVerifications WHERE user_id = :userId";
}
