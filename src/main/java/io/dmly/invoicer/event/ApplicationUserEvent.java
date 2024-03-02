package io.dmly.invoicer.event;

import io.dmly.invoicer.model.enumaration.EventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ApplicationUserEvent extends ApplicationEvent {
    private final EventType type;
    private final String email;

    public ApplicationUserEvent(EventType type, String email) {
        super(email);
        this.type = type;
        this.email = email;
    }
}
