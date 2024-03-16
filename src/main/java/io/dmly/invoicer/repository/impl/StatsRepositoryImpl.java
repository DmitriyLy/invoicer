package io.dmly.invoicer.repository.impl;

import io.dmly.invoicer.model.Stats;
import io.dmly.invoicer.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static io.dmly.invoicer.query.StatsQueries.STATS_QUERY;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StatsRepositoryImpl implements StatsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Stats> statsRowMapper;

    @Override
    public Stats getStats() {
        return jdbcTemplate.queryForObject(STATS_QUERY, Map.of(), statsRowMapper);
    }
}
