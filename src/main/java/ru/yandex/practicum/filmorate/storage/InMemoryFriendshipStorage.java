package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("InMemoryFriendshipStorage")
public class InMemoryFriendshipStorage implements FriendshipStorage {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public InMemoryFriendshipStorage(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Boolean isFriendshipExists(Long id, Long friendId) {
        User user = inMemoryUserStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));
        return user.getFriends().contains(friendId);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        inMemoryUserStorage.getUserById(id).get()
                .setToFriends(inMemoryUserStorage.getUserById(friendId).get().getId());
        log.trace("Юзер с id: " + id + " добавил в друзья юзера с id" + friendId);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        inMemoryUserStorage.getUserById(id).get()
                .removeFromFriends(inMemoryUserStorage.getUserById(friendId).get().getId());
        log.trace("Юзер с id: " + id + " удалил из друзей юзера с id" + friendId);
    }

    public Optional<List<User>> getFriends(Long id) {
        List<User> result = new ArrayList<>();
        if (inMemoryUserStorage.findAll().isEmpty()) {
            log.error("Ошибка при получении списка юзеров");
            return Optional.empty();
        }
        if (inMemoryUserStorage.getUserById(id).get().getFriends().size() > 0) {
            for (Long userFriendId : inMemoryUserStorage.getUserById(id).get().getFriends()) {
                result.add(inMemoryUserStorage.getUserById(userFriendId).get());
            }
            return Optional.of(result);
        }
        log.error("Ошибка при получении списка юзеров");
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> getMutualFriends(Long idUser0, Long idUser1) {
        User user0 = inMemoryUserStorage.getUserById(idUser0).get();
        User user1 = inMemoryUserStorage.getUserById(idUser1).get();
        List<User> result = new ArrayList<>();
        if (inMemoryUserStorage.findAll().isPresent()) {
            for (User user : inMemoryUserStorage.findAll().get()) {
                if (user.getFriends().contains(user0.getId())
                        && user.getFriends().contains(user1.getId())) {
                    result.add(user);
                }
            }
        }
        if (result.size() == 0) {
            log.error("Ошибка при получении общих друзей");
            return Optional.empty();
        }
        log.trace("Возвращен список общих друзей");
        return Optional.of(result);
    }
}