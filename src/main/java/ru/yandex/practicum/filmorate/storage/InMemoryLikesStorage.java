package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

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

    @Override
    public boolean isFilmHasLike(Long userId, Long filmId) {
        Film film = inMemoryFilmStorage.getFilmById(filmId)
                                        .orElseThrow(() -> new NotFoundException("Film with id " + filmId + " not found."));
        return film.getLikes()
                   .stream()
                   .anyMatch(id -> id.equals(userId));
    }
}