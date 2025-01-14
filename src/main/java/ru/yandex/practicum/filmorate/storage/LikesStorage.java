package ru.yandex.practicum.filmorate.storage;

public interface LikesStorage {
    public void giveLike(Long userId, Long filmId);

    public void removeLike(Long userId, Long filmId);
}