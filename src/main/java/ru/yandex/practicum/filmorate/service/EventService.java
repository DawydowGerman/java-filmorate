package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {
    private final FeedStorage feedStorage;

    public void add(Long entityId, Long userId, EventType eventType) {
        feedStorage.create(createEvent(entityId, eventType, Operation.ADD, userId));
    }

    public void remove(Long entityId, Long userId, EventType eventType) {
        feedStorage.create(createEvent(entityId, eventType, Operation.REMOVE, userId));
    }

    public void update(Long entityId, Long userId, EventType eventType) {
        feedStorage.create(createEvent(entityId, eventType, Operation.UPDATE, userId));
    }

    private Event createEvent(Long entityId, EventType eventType, Operation operationType, Long userId) {
        return Event.builder()
                .entityId(entityId)
                .eventType(eventType)
                .operation(operationType)
                .userId(userId)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
    }
}
