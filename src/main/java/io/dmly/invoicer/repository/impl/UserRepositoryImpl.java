package io.dmly.invoicer.repository.impl;

import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.model.Role;
import io.dmly.invoicer.model.User;
import io.dmly.invoicer.model.enumaration.VerificationUrlType;
import io.dmly.invoicer.repository.RoleRepository;
import io.dmly.invoicer.repository.UserRepository;
import io.dmly.invoicer.rowmapper.UserRowMapper;
import io.dmly.invoicer.service.EmailService;
import io.dmly.invoicer.service.VerificationUrlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.*;

import static io.dmly.invoicer.model.enumaration.RoleType.ROLE_USER;
import static io.dmly.invoicer.query.UserQueries.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User> {
    private static final String SQL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RoleRepository<Role> roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationUrlGenerator verificationUrlGenerator;
    private final EmailService emailService;
    private final UserRowMapper userRowMapper;

    @Override
    public Optional<User> getUserByEmail(String email) {
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL, Map.of("email", email), userRowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.error("Cannot find a user by email {}", email);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User create(User user) {
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) {
            throw new ApiException("Specified email already in use. Please provide another one.");
        }

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getCreateUserParameters(user);

            jdbcTemplate.update(INSERT_USER_QUERY, parameters, keyHolder);
            user.setId((Long) Objects.requireNonNull(keyHolder.getKeys()).get("id"));

            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());

            String verificationUrl = verificationUrlGenerator.generateVerificationUrl(VerificationUrlType.ACCOUNT);

            jdbcTemplate.update(INSERT_ACCOUNT_VERIFICATION_URL, Map.of(
                    "userId", user.getId(),
                    "url", verificationUrl
            ));

            emailService.sendVerificationUrl(user, verificationUrl, VerificationUrlType.ACCOUNT);

            user.setEnabled(false);
            user.setNonLocked(true);
        } catch (Exception e) {
            log.error("An error occurred.", e);
            throw new ApiException("An error occurred. Please try later", e);
        }

        return user;
    }


    @Override
    public Collection<User> list(int page, int pageSize) {
        return null;
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void updateVerificationCodeForUser(User user, String verificationCode, Date codeExpirationDate) {
        try {
            jdbcTemplate.update(UPSERT_VERIFICATION_CODE_FOR_USER, Map.of(
                    "userId", user.getId(),
                    "code", verificationCode,
                    "expirationDate", DateFormatUtils.format(codeExpirationDate, SQL_DATE_FORMAT)
            ));
        } catch (Exception e) {
            log.error("An error occurred.", e);
            throw new ApiException("An error occurred. Please try later", e);
        }
    }

    @Override
    public Optional<User> getUserByEmailAndValidCode(String email, String code) {
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL_AND_VALID_CODE,
                    Map.of(
                            "email", email,
                            "code", code
                    ),
                    userRowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.error("Cannot find a user by email {} and code {}", email, code);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public void deleteVerificationCodeByUserId(Long userId) {
        try {
            jdbcTemplate.update(DELETE_VERIFICATION_CODE_BY_USER_ID, Map.of("userId", userId));
        } catch (Exception e) {
            log.error("An error occurred.", e);
            throw new ApiException("An error occurred. Please try later", e);
        }
    }

    @Override
    public void updateResetPasswordVerification(User user, String url, Date codeExpirationDate) {
        try {
            jdbcTemplate.update(UPSERT_RESET_PASSWORD_VERIFICATION_FOR_USER, Map.of(
                    "userId", user.getId(),
                    "url", url,
                    "expirationDate", DateFormatUtils.format(codeExpirationDate, SQL_DATE_FORMAT)
            ));
        } catch (Exception e) {
            log.error("An error occurred.", e);
            throw new ApiException("An error occurred. Please try later", e);
        }
    }

    private Integer getEmailCount(String email) {
        return jdbcTemplate.queryForObject(GET_EMAILS_COUNT_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getCreateUserParameters(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", passwordEncoder.encode(user.getPassword()))
                ;
    }
}
