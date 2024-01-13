package io.dmly.invoicer.rowmapper;

import io.dmly.invoicer.model.ResetPasswordVerificationEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ResetPasswordVerificationEntityRowMapper implements RowMapper<ResetPasswordVerificationEntity> {
    @Override
    public ResetPasswordVerificationEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new ResetPasswordVerificationEntity(
                resultSet.getLong("id"),
                resultSet.getLong("user_id"),
                resultSet.getString("key"),
                resultSet.getTimestamp("expiration_date").toLocalDateTime()
        );
    }
}
