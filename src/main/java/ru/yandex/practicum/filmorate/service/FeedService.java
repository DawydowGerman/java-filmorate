package ru.yandex.practicum.filmorate.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.EventDTO;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FeedService {
    FeedStorage feedStorage;
    UserService userService;

    public List<EventDTO> readEventFeedForUser(Long userId) {
        userService.getUserById(userId);
        return feedStorage.readEventFeedForUser(userId).stream().map(EventMapper::mapToEventResponse).toList();
    }
}
