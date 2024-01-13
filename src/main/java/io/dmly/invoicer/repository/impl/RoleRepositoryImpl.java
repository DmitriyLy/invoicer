package io.dmly.invoicer.repository.impl;

import io.dmly.invoicer.model.Role;
import io.dmly.invoicer.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static io.dmly.invoicer.query.RoleQueries.SELECT_ROLE_BY_NAME_QUERY;
import static io.dmly.invoicer.query.RoleQueries.SELECT_ROLE_BY_USER_ID;
import static io.dmly.invoicer.query.UserQueries.INSERT_SET_ROLE_FOR_USER_QUERY;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Role> roleRowMapper;

    @Override
    public Role create(Role role) {
        return null;
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        return null;
    }

    @Override
    public Role get(Long id) {
        return null;
    }

    @Override
    public Role update(Role role) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("Adding role {} to a user with id {}", roleName, userId);
        try {
            Role role = jdbcTemplate.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("roleName", roleName), roleRowMapper);
            jdbcTemplate.update(INSERT_SET_ROLE_FOR_USER_QUERY,
                    Map.of("userId", userId,
                            "roleId", Objects.requireNonNull(role).getId())
            );
        } catch (Exception e) {
            log.error("An error occurred while setting role to a user", e);
        }

    }

    @Override
    public Optional<Role> getRoleByUserId(Long userId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(SELECT_ROLE_BY_USER_ID, Map.of("userId", userId), roleRowMapper)
            );
        } catch (EmptyResultDataAccessException e)  {
            log.error("Cannot find role by user id {}", userId);
            return Optional.empty();
        }
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        return null;
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {

    }
}
