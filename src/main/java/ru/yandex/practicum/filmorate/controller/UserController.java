package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.EventDTO;
import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.dto.user.UserRequestDTO;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FeedService feedService;

    @Autowired
    public UserController(UserService userService, FeedService feedService) {
        this.userService = userService;
        this.feedService = feedService;
    }

    @PostMapping
    public UserDTO create(@Valid @RequestBody UserRequestDTO userDto) {
        return userService.create(userDto);
    }

    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @PutMapping
    public UserDTO update(@RequestBody UserDTO userDto) {
        return userService.update(userDto);
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable(name = "userId") Long id) {
        userService.remove(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends")
    public List<UserDTO> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserDTO> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @GetMapping("/{id}/recommendations")
    public List<FilmDTO> getRecommendations(@PathVariable Long id) {
        return userService.getRecommendations(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/feed")
    public List<EventDTO> readEventFeedForUser(@PathVariable("id") long userId) {
        return feedService.readEventFeedForUser(userId);
    }
}