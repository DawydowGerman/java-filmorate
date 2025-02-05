package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface FeedStorage {
    Event create(Event event);

    List<Event> readEventFeedForUser(Long userId);
}