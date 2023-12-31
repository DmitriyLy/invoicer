package io.dmly.invoicer.rowmapper;

import io.dmly.invoicer.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .email(resultSet.getString("email"))
                .phone(resultSet.getString("phone"))
                .password(resultSet.getString("password"))
                .address(resultSet.getString("address"))
                .title(resultSet.getString("title"))
                .bio(resultSet.getString("bio"))
                .isEnabled(resultSet.getBoolean("enabled"))
                .isNonLocked(resultSet.getBoolean("non_locked"))
                .isUsingMfa(resultSet.getBoolean("using_mfa"))
                .imageUrl(resultSet.getString("image_url"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}
