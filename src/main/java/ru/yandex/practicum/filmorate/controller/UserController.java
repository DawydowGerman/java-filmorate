package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() throws Exception {
        Exception e;
        if (users.size() == 0) {
            e = new NotFoundException("Список юзеров пуст");
            log.error("Ошибка при добавлении юзера", e);
            throw e;
        } else return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) throws Exception {
        Exception e;
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            e = new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
            log.error("Ошибка при добавлении юзера", e);
            throw e;
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            e = new ValidationException("Логин не может быть пустым и содержать пробелы");
            log.error("Ошибка при добавлении юзера", e);
            throw e;
        }
        if (user.getBirthday() == null) {
            e = new ValidationException("Дата рождения должна быть указана");
            log.error("Ошибка при добавлении юзера", e);
            throw e;
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            e = new ValidationException("Дата рождения не может быть в будущем");
            log.error("Ошибка при добавлении юзера", e);
            throw e;
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("Добавлен юзер с Id {}", user.getId());
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public User update(@RequestBody User newUser) throws Exception {
        Exception e;
        if (newUser.getId() == null) {
            e = new ValidationException("Id должен быть указан");
            log.error("Ошибка при добавлении юзера", e);
            throw e;
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            for (User user0 : users.values()) {
                if (!user0.getId().equals(newUser.getId()) && user0.getEmail().equals(newUser.getEmail())) {
                    e = new ValidationException("Этот имейл уже используется");
                    log.error("Ошибка при добавлении юзера", e);
                    throw e;
                }
            }
            if (newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
                log.trace("Изменен имейл юзера с Id {}", newUser.getId());
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getLogin() != null && !newUser.getLogin().isEmpty()) {
                log.trace("Изменен логин юзера с Id {}", newUser.getId());
                oldUser.setLogin(newUser.getLogin());
            }
            if (newUser.getName() != null && !newUser.getName().isEmpty()) {
                log.trace("Изменено имя юзера с Id {}", newUser.getId());
                oldUser.setName(newUser.getName());
            }
            if (newUser.getBirthday() != null && !newUser.getBirthday().isAfter(LocalDate.now())) {
                log.trace("Изменена дата рождения юзера с Id {}", newUser.getId());
                oldUser.setBirthday(newUser.getBirthday());
            }
            log.debug("Обновлен юзер с Id {}", newUser.getId());
            return oldUser;
        }
        e = new NotFoundException("Юзер с id = " + newUser.getId() + " не найден");
        log.error("Ошибка при добавлении юзера", e);
        throw e;
    }
}
