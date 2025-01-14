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
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class UserService {
    private UserStorage userStorage;
    private FriendshipStorage friendshipStorage;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserService(@Qualifier("DatabaseUserStorage") UserStorage userStorage,
                       @Qualifier("DatabaseFriendshipStorage")FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public UserDTO create(UserDTO userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank() || !userDto.getEmail().contains("@")) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (this.findAll() != null && this.findAll().size() > 0) {
            for (User user0 : this.findAllUtil()) {
                if (!user0.getId().equals(userDto.getId()) && user0.getEmail().equals(userDto.getEmail())) {
                    log.error("Ошибка при добавлении юзера");
                    throw new ValidationException("Этот имейл уже используется");
                }
            }
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
        User user = UserMapper.toModel(userDto);
        user = userStorage.create(user);
        return UserMapper.toDto(user);
    }

    public UserDTO getUserById(Long userId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isPresent()) {
            return UserMapper.toDto(user.get());
        } else throw new NotFoundException("Юзер с " + userId + " отсутствует.");
    }

    public List<UserDTO> findAll() {
        Optional<List<User>> userList = userStorage.findAll();
        if (userList.isPresent()) {
            List<UserDTO> dtoList = userList.get()
                    .stream()
                    .map(user -> UserMapper.toDto(user))
                    .collect(Collectors.toList());
            return dtoList;
        } else throw new NotFoundException("Список юзеров пуст.");
    }

    public List<User> findAllUtil() {
        Optional<List<User>> userList = userStorage.findAll();
        if (userList.isPresent()) {
            return userList.get();
        } else throw new NotFoundException("Список юзеров пуст.");
    }

    public UserDTO update(UserDTO userDto) {
        if (userDto.getId() == null) {
            log.error("Ошибка при обновлении данных юзера");
            throw new ValidationException("Id должен быть указан");
        }
        if (userStorage.isUserIdExists(userDto.getId())) {
            if (userDto.getEmail() == null || userDto.getEmail().isBlank() || !userDto.getEmail().contains("@")) {
                log.error("Ошибка при обновлении данных юзера");
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
            }
            if (userDto.getLogin() == null || userDto.getLogin().isBlank() || userDto.getLogin().contains(" ")) {
                log.error("Ошибка при обновлении данных юзера");
                throw new ValidationException("Логин не может быть пустым и содержать пробелы");
            }
            if (userDto.getBirthday() == null) {
                log.error("Ошибка при обновлении данных юзера");
                throw new ValidationException("Дата рождения должна быть указана");
            } else if (userDto.getBirthday().isAfter(LocalDate.now())) {
                log.error("Ошибка при обновлении данных юзера");
                throw new ValidationException("Дата рождения не может быть в будущем");
            }
            if (userDto.getName() == null || userDto.getName().isBlank()) {
                userDto.setName(userDto.getLogin());
            }
            if (userDto.getFriends() == null) {
                userDto.setFriends(new HashSet<>());
            }
            for (User user0 : this.findAllUtil()) {
                if (!user0.getId().equals(userDto.getId()) && user0.getEmail().equals(userDto.getEmail())) {
                    log.error("Ошибка при обновлении данных юзера");
                    throw new ValidationException("Этот имейл уже используется");
                }
            }
            User user0 = UserMapper.toModel(userDto);
            user0 = userStorage.update(user0);
            return UserMapper.toDto(user0);
        } else {
            log.error("Ошибка при обновлении данных юзера");
            throw new NotFoundException("Юзер отсутствуют");
        }
    }

    public void addFriend(Long id, Long friendId) {
        if (userStorage.isUserIdExists(id) && userStorage.isUserIdExists(friendId)) {
            friendshipStorage.addFriend(id, friendId);
        } else {
            log.error("Ошибка при добавлении в друзья");
            throw new NotFoundException("Один из юзеров либо оба отсутствуют");
        }
    }

    public void removeFriend(Long id, Long friendId) {
        if (userStorage.isUserIdExists(id) && userStorage.isUserIdExists(friendId)) {
            friendshipStorage.removeFriend(id, friendId);
        } else {
            log.error("Ошибка при удалении из друзей");
            throw new NotFoundException("Один из юзеров либо оба отсутствуют");
        }
    }

    public List<UserDTO> getFriends(Long id) {
        if (userStorage.isUserIdExists(id)) {
            if (friendshipStorage.getFriends(id).isPresent()) {
                List<UserDTO> dtoList = friendshipStorage.getFriends(id).get()
                        .stream()
                        .map(user -> UserMapper.toDto(user))
                        .collect(Collectors.toList());
                return dtoList;
            } else throw new NotFoundException("У юзера с " + id + " отсутствует друзья.");
        } else {
            log.error("Ошибка при добавлении в друзья");
            throw new NotFoundException("Юзер с " + id + " отсутствует.");
        }
    }

    public List<UserDTO> getMutualFriends(Long idUser0, Long idUser1) {
        if (userStorage.isUserIdExists(idUser0) && userStorage.isUserIdExists(idUser1)) {
            if (friendshipStorage.getMutualFriends(idUser0, idUser1).isPresent()) {
                List<UserDTO> dtoList = friendshipStorage.getMutualFriends(idUser0, idUser1).get()
                        .stream()
                        .map(user -> UserMapper.toDto(user))
                        .collect(Collectors.toList());
                return dtoList;
            }
            else throw new NotFoundException("У юзера с " + idUser0 + " "
                    + idUser1 + " отсутствует общие друзья.");
        } else {
            log.error("Ошибка при удалении из друзей");
            throw new NotFoundException("Один из юзеров либо оба отсутствуют");
        }
    }

    public boolean remove(Long id) {
        if (userStorage.isUserIdExists(id)) {
            return userStorage.remove(id);
        } else {
            log.error("Ошибка при удалении юзера");
            throw new NotFoundException("Юзер отсутствует");
        }
    }
}