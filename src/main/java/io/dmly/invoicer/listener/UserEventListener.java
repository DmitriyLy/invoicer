package io.dmly.invoicer.listener;

import io.dmly.invoicer.event.ApplicationUserEvent;
import io.dmly.invoicer.resolver.ClientParametersResolver;
import io.dmly.invoicer.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventListener {
    private final EventService eventService;
    private final HttpServletRequest request;
    private final ClientParametersResolver clientParametersResolver;

    @EventListener
    public void onUserEvent(ApplicationUserEvent userEvent) {
        log.info("New event {}", userEvent);
        eventService.addUserEvent(userEvent.getEmail(), userEvent.getType(), clientParametersResolver.getDevice(request), clientParametersResolver.getIpAddress(request));
    }
}
