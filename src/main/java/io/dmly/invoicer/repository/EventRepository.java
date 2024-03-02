package io.dmly.invoicer.repository;

import io.dmly.invoicer.model.UserEvent;
import io.dmly.invoicer.model.enumaration.EventType;

import java.util.Collection;

public interface EventRepository {
    Collection<UserEvent> getEventsByUserId(Long id);

    void addUserEvent(String email, EventType eventType, String device, String ipAddress);
}
