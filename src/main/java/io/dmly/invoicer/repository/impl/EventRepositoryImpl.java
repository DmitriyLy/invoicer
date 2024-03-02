package io.dmly.invoicer.repository.impl;

import io.dmly.invoicer.model.UserEvent;
import io.dmly.invoicer.model.enumaration.EventType;
import io.dmly.invoicer.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

import static io.dmly.invoicer.query.EventQueries.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EventRepositoryImpl implements EventRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<UserEvent> userEventRowMapper;

    @Override
    public Collection<UserEvent> getEventsByUserId(Long id) {
        return jdbcTemplate.query(SELECT_EVENTS_BY_USER_ID_QUERY, Map.of("id", id), userEventRowMapper);
    }

    @Override
    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
        jdbcTemplate.update(INSERT_EVENT_BY_USER_EMAIL_QUERY, Map.of(
                "email", email,
                "type", eventType.toString(),
                "device", device,
                "ipAddress", ipAddress
                ));
    }
}
