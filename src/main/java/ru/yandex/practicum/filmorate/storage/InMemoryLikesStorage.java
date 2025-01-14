package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;

@Component("InMemoryLikesStorage")
public class InMemoryLikesStorage implements LikesStorage {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public InMemoryLikesStorage(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @Override
    public void giveLike(Long userId, Long filmId) {
        inMemoryFilmStorage.getFilmById(filmId).get().addLike(userId);
        log.trace("Фильму с Id {} поставлен лайк", filmId);
    }

    @Override
    public void removeLike(Long userId, Long filmId) {
        inMemoryFilmStorage.getFilmById(filmId).get().removeLike(userId);
        log.trace("Фильму с Id {} удален лайк", filmId);
    }
}