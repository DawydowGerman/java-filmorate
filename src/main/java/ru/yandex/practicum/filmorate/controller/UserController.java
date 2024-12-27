package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private UserStorage userStorage;

    @Autowired
    public UserController(@Qualifier("DatabaseUserStorage")UserStorage userStorage, UserService userService) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @PostMapping
    public UserDTO create(@Valid @RequestBody UserDTO userDto) {
        return userService.create(userDto);
    }

    @PutMapping
    public UserDTO update(@RequestBody UserDTO userDto) {
        return userService.update(userDto);
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

    @GetMapping
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        if (userService.getFriends(id) != null && userService.getFriends(id).size() > 0) {
            return userService.getFriends(id);
        } else return Collections.emptyList();
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isPresent()) {
            return user.get();
        } else throw new NotFoundException("Юзер с " + userId + " отсутствует.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowable(final Throwable e) {
        return Map.of("errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        return Map.of("errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        return Map.of("errorMessage", e.getMessage());
    }
}