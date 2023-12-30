package io.dmly.invoicer.query;

public class UserQueries {
    public static final String GET_EMAILS_COUNT_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String INSERT_USER_QUERY = "INSERT INTO Users(first_name, last_name, email, password) " +
            "VALUES (:firstName, :lastName, :email, :password)";
    public static final String INSERT_ACCOUNT_VERIFICATION_URL = "INSERT INTO AccountVerifications(user_id, url) " +
            "VALUES(:userId, :url)";
    public static final String INSERT_SET_ROLE_FOR_USER = "INSERT INTO UserRoles(user_id, role_id) " +
            "VALUES(:userId, :roleId)";
}
