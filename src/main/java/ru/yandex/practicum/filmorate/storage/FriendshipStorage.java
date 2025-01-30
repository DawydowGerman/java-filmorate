package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FriendshipStorage {
    public void addFriend(Long id, Long friendId);

    public void removeFriend(Long id, Long friendId);

    public Optional<List<User>> getFriends(Long id);

    public Optional<List<User>> getMutualFriends(Long idUser0, Long idUser1);
}