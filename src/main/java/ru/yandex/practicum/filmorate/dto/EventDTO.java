package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EventDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long eventId;
    private Long entityId;
    private String eventType;
    private String operation;
    private Long userId;
    private Long timestamp;
}

