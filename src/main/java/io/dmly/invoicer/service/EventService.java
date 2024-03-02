package io.dmly.invoicer.service;

import io.dmly.invoicer.model.UserEvent;
import io.dmly.invoicer.model.enumaration.EventType;

import java.util.Collection;

public interface EventService {
    Collection<UserEvent> getEventsByUserId(Long id);

    void addUserEvent(String email, EventType eventType, String device, String ipAddress);

    void addUserEvent(Long userId, EventType eventType, String device, String ipAddress);
}
