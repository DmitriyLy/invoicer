package io.dmly.invoicer.service.impl;

import io.dmly.invoicer.model.UserEvent;
import io.dmly.invoicer.model.enumaration.EventType;
import io.dmly.invoicer.repository.EventRepository;
import io.dmly.invoicer.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public Collection<UserEvent> getEventsByUserId(Long id) {
        return eventRepository.getEventsByUserId(id);
    }

    @Override
    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
        eventRepository.addUserEvent(email, eventType, device, ipAddress);
    }

    @Override
    public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {
        throw new NotImplementedException();
    }
}
