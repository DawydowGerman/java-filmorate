package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.sql.Timestamp;

@Data
@Builder
public class Event {
    Long id;

    EventType eventType;

    Operation operation;

    Long userId;

    Long entityId;

    Timestamp createdAt;
}