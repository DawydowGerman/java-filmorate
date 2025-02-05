package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EventDTO {
    private long eventId;
    private long entityId;
    private String eventType;
    private String operation;
    private long userId;
    private long timestamp;
}

