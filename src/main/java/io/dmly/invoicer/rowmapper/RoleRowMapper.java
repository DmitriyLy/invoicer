package io.dmly.invoicer.rowmapper;

import io.dmly.invoicer.model.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RoleRowMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return buildRole(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("permission")
        );
    }

    public Role buildRole(Long id, String name, String permission) {
        return Role.builder()
                .id(id)
                .name(name)
                .permission(permission)
                .build();
    }
}
