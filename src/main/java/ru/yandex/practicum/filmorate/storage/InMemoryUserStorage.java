package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("Добавлен юзер с Id {}", user.getId());
        return user;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        if (users.size() == 0) {
            log.error("Ошибка при получении списка юзеров");
            return Optional.empty();
        }
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }
        log.error("Ошибка при получении списка юзеров");
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> findAll() {
        if (users.size() == 0) {
            log.error("Ошибка при получении списка юзеров");
            return Optional.empty();
        } else return Optional.of((List<User>) users.values());
    }

    @Override
    public User update(User newUser) {
        User oldUser = users.get(newUser.getId());
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
        if (newUser.getFriends() != null) {
            oldUser.setFriends(newUser.getFriends());
        }
        log.debug("Обновлен юзер с Id {}", newUser.getId());
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public void remove(Long id) {
        users.remove(id);
        log.debug("Пользователь с id = {} удален", id);
    }

    @Override
    public Boolean isUserIdExists(Long id) {
        return users.containsKey(id);
    }
}