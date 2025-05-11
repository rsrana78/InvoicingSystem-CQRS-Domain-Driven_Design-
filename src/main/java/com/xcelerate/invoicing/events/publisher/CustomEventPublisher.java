package com.xcelerate.invoicing.events.publisher;

import com.xcelerate.invoicing.dto.events.AbstractEventDTO;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CustomEventPublisher {

    private final ApplicationEventPublisher eventPublisher;


    public CustomEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishEvent(final AbstractEventDTO event){
        eventPublisher.publishEvent(event);
    }
}
