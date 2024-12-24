package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;

@Service
@Data
public class UserService {
    private UserStorage userStorage;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserService(@Qualifier("DatabaseUserStorage")UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long idUser0, Long idUser1) {
        Optional<User> user0 = userStorage.getUserById(idUser0);
        Optional<User> user1 = userStorage.getUserById(idUser1);
        if (user0.isPresent() && user1.isPresent()) {
            user0.get().setToFriends(user1.get().getId());
            user1.get().setToFriends(user0.get().getId());
            log.trace("Юзеры с id: " + idUser0 + ", " + idUser1 + " добавлены в друзья");
        } else {
            log.error("Ошибка при добавлении в друзья");
            throw new NotFoundException("Один из юзеров либо оба отсутствуют");
        }
    }

    public void removeFriend(Long idUser0, Long idUser1) {
        Optional<User> user0 = userStorage.getUserById(idUser0);
        Optional<User> user1 = userStorage.getUserById(idUser1);
        if (user0.isEmpty()) {
            log.error("Ошибка при удалении из друзей");
            throw new NotFoundException("Юзер с id " + idUser0 + " отсутствует");
        }
        if (user1.isEmpty()) {
            log.error("Ошибка при удалении из друзей");
            throw new NotFoundException("Юзер с id " + idUser1 + " отсутствует");
        }
        if (user0.isPresent()
                && user1.isPresent()
                && user0.get().isFriend(user1.get().getId())) {
            user0.get().removeFromFriends(user1.get().getId());
            user1.get().removeFromFriends(user0.get().getId());
            log.trace("Юзеры с id: " + idUser0 + ", " + idUser1 + " удалены из друзей");
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