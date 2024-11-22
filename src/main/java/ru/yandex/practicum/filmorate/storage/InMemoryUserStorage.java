package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        if (users.size() == 0) {
            log.error("Ошибка при получении списка юзеров");
            return null;
        } else return users.values();
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() == null) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Дата рождения должна быть указана");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Дата рождения не может быть в будущем");
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

    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.error("Ошибка при обновлении данных юзера");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            for (User user0 : users.values()) {
                if (!user0.getId().equals(newUser.getId()) && user0.getEmail().equals(newUser.getEmail())) {
                    log.error("Ошибка при обновлении данных юзера");
                    throw new ValidationException("Этот имейл уже используется");
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
        log.error("Ошибка при добавлении юзера");
        throw new NotFoundException("Юзер с id = " + newUser.getId() + " не найден");
    }

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

    public Optional<List<User>> getFriends(Long id) {
        List<User> result = new ArrayList<>();
        if (users.size() == 0) {
            log.error("Ошибка при получении списка юзеров");
            return Optional.empty();
        }
        if (users.containsKey(id) && users.get(id).getFriends().size() > 0) {
            for(Long userFriendId : users.get(id).getFriends()) {
                result.add(users.get(userFriendId));
            }
            return Optional.of(result);
        }
        log.error("Ошибка при получении списка юзеров");
        return Optional.empty();
    }
}
