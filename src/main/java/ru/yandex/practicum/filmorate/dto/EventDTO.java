package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EventDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long eventId;
    private long entityId;
    private String eventType;
    private String operation;
    private long userId;
    private long timestamp;
}

