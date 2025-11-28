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
import java.util.stream.Collectors;

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
        User user = inMemoryUserStorage.getUserById(id).get();
        List<Long> friendIds = user.getFriends().stream().collect(Collectors.toList());;
        if (friendIds == null || friendIds.isEmpty()) return Optional.empty();
        List<User> friends = friendIds.stream()
                .map(l -> inMemoryUserStorage.getUserById(l).get())
                .collect(Collectors.toList());
        return Optional.of(friends);
    }

    @Override
    public Optional<List<User>> getMutualFriends(Long idUser0, Long idUser1) {
        User user0 = inMemoryUserStorage.getUserById(idUser0).get();
        User user1 = inMemoryUserStorage.getUserById(idUser1).get();

        List<User> result = inMemoryUserStorage.findAll().get().stream()
                .filter(user -> user.getFriends().contains(user0.getId())
                        && user.getFriends().contains(user1.getId()))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            log.error("Ошибка при получении общих друзей");
            return Optional.empty();
        }
        log.trace("Возвращен список общих друзей");
        return Optional.of(result);
    }
}