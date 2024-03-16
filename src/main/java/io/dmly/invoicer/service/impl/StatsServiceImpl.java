package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.model.Stats;
import io.dmly.invoicer.repository.StatsRepository;
import io.dmly.invoicer.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public Stats getStats() {
        return statsRepository.getStats();
    }
}
