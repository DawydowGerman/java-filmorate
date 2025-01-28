package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<List<User>> findAll();

    Optional<User> getUserById(Long id);

    User create(User user);

    User update(User newUser);

    boolean remove(Long id);

    boolean isUserIdExists(Long id);
}