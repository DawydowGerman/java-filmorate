package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DatabaseFriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.dto.UserDTO;

import java.time.LocalDate;
import java.util.*;

@Service
@Data
public class UserService {
    private UserStorage userStorage;
    private DatabaseFriendshipStorage databaseFriendshipStorage;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserService(@Qualifier("DatabaseUserStorage")UserStorage userStorage, DatabaseFriendshipStorage databaseFriendshipStorage) {
        this.userStorage = userStorage;
        this.databaseFriendshipStorage = databaseFriendshipStorage;
    }

    public UserDTO create(UserDTO userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank() || !userDto.getEmail().contains("@")) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (userStorage.findAll() != null && userStorage.findAll().size() > 0) {
            for (User user0 : userStorage.findAll()) {
                if (!user0.getId().equals(userDto.getId()) && user0.getEmail().equals(userDto.getEmail())) {
                    log.error("Ошибка при обновлении данных юзера");
                    throw new ValidationException("Этот имейл уже используется");
                }
            }
        }
        System.out.println("UserService class, 47 line");
        if (userDto.getLogin() == null || userDto.getLogin().isBlank() || userDto.getLogin().contains(" ")) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (userDto.getBirthday() == null) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Дата рождения должна быть указана");
        } else if (userDto.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            userDto.setName(userDto.getLogin());
        }
        if (userDto.getFriends() == null) {
            userDto.setFriends(new HashSet<>());
        }
        User user = UserMapper.toModel(userDto);
        user = userStorage.create(user);
        return UserMapper.toDto(user);
    }

    public UserDTO update(UserDTO userDto) {
        if (userDto.getId() == null) {
            log.error("Ошибка при обновлении данных юзера");
            throw new ValidationException("Id должен быть указан");
        }
        if (userStorage.isUserIdExists(userDto.getId())) {
            if (userDto.getEmail() == null || userDto.getEmail().isBlank() || !userDto.getEmail().contains("@")) {
                log.error("Ошибка при добавлении юзера");
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
            }
            if (userDto.getLogin() == null || userDto.getLogin().isBlank() || userDto.getLogin().contains(" ")) {
                log.error("Ошибка при добавлении юзера");
                throw new ValidationException("Логин не может быть пустым и содержать пробелы");
            }
            if (userDto.getBirthday() == null) {
                log.error("Ошибка при добавлении юзера");
                throw new ValidationException("Дата рождения должна быть указана");
            } else if (userDto.getBirthday().isAfter(LocalDate.now())) {
                log.error("Ошибка при добавлении юзера");
                throw new ValidationException("Дата рождения не может быть в будущем");
            }
            if (userDto.getName() == null || userDto.getName().isBlank()) {
                userDto.setName(userDto.getLogin());
            }
            if (userDto.getFriends() == null) {
                userDto.setFriends(new HashSet<>());
            }
            for (User user0 : userStorage.findAll()) {
                if (!user0.getId().equals(userDto.getId()) && user0.getEmail().equals(userDto.getEmail())) {
                    log.error("Ошибка при обновлении данных юзера");
                    throw new ValidationException("Этот имейл уже используется");
                }
            }
            User user0 = UserMapper.toModel(userDto);
            user0 = userStorage.update(user0);
            return UserMapper.toDto(user0);
        } else {
            log.error("Ошибка при добавлении в друзья");
            throw new NotFoundException("Один из юзеров либо оба отсутствуют");
        }
    }

    public void addFriend(Long id, Long friendId) {
        if (userStorage.isUserIdExists(id) && userStorage.isUserIdExists(friendId)) {
            databaseFriendshipStorage.addFriend(id, friendId);
        } else {
            log.error("Ошибка при добавлении в друзья");
            throw new NotFoundException("Один из юзеров либо оба отсутствуют");
        }
    }

    public void removeFriend(Long id, Long friendId) {
        if (userStorage.isUserIdExists(id) && userStorage.isUserIdExists(friendId)) {
            databaseFriendshipStorage.removeFriend(id, friendId);
        } else {
            log.error("Ошибка при добавлении в друзья");
            throw new NotFoundException("Один из юзеров либо оба отсутствуют");
        }
    }

    public List<User> getFriends(Long id) {
        if (userStorage.isUserIdExists(id)) {
            if (databaseFriendshipStorage.getFriends(id).isPresent()) {
                return databaseFriendshipStorage.getFriends(id).get();
            } else return null;
        } else {
            log.error("Ошибка при добавлении в друзья");
            throw new NotFoundException("Юзер отсутствуют");
        }
    }

    public List<User> getMutualFriends(Long idUser0, Long idUser1) {
        Optional<User> user0 = userStorage.getUserById(idUser0);
        Optional<User> user1 = userStorage.getUserById(idUser1);
        List<User> result = new ArrayList<>();
        if (user0.isPresent() && user1.isPresent()) {
            for (User user : userStorage.findAll()) {
                if (user.getFriends().contains(user0.get().getId())
                        && user.getFriends().contains(user1.get().getId())) {
                    result.add(user);
                }
            }
            if (result.size() == 0) {
                log.error("Ошибка при получении общих друзей");
                throw new NotFoundException("Юзеры не имеют общих друзей");
            }
            log.trace("Возвращен список общих друзей");
            return result;
        } else {
            log.error("Ошибка при получении общих друзей");
            throw new NotFoundException("Один из юзеров либо оба отсутствуют");
        }
    }
}