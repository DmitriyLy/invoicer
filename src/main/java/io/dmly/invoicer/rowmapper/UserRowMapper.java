package io.dmly.invoicer.rowmapper;

import io.dmly.invoicer.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class UserRowMapper implements RowMapper<User> {
    private final RoleRowMapper roleRowMapper;

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        var role = roleRowMapper.buildRole(
                resultSet.getLong("role_id"),
                resultSet.getString("role_name"),
                resultSet.getString("permission")
        );

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
                .role(role)
                .build();
    }
}
