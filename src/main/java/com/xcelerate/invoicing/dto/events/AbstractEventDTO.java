package com.xcelerate.invoicing.dto.events;

import java.time.LocalDateTime;

public class AbstractEventDTO {

    protected String eventId;
    protected String eventType;
    protected LocalDateTime eventTimeStamp;

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public LocalDateTime getEventTimeStamp() {
        return eventTimeStamp;
    }
}
