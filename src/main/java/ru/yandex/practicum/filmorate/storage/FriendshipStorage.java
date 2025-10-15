package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FriendshipStorage {
    void addFriend(Long id, Long friendId);

    void removeFriend(Long id, Long friendId);

    Optional<List<User>> getFriends(Long id);

    Optional<List<User>> getMutualFriends(Long idUser0, Long idUser1);

    Boolean isFriendshipExists(Long id, Long friendId);
}