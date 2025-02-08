package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.sql.Timestamp;

@Data
@Builder
public class Event {
    private Long id;

    private EventType eventType;

    private Operation operation;

    private Long userId;

    private Long entityId;

    private Timestamp createdAt;
}