package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Data
public class FilmService {
    private FilmStorage inMemoryFilmStorage;
    private UserStorage inMemoryUserStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void giveLike(Long userId, Long filmId) {
        Optional<User> user = inMemoryUserStorage.getUserById(userId);
        if (user.isEmpty()) {
            log.error("Ошибка при удалении из друзей");
            throw new NotFoundException("Юзер с id " + userId + " отсутствует");
        }
        Optional<Film> film0 = inMemoryFilmStorage.getFilmById(filmId);
        if (film0.isPresent()) {
            film0.get().addLike(userId);
            log.trace("Фильму с Id {} поставлен лайк", filmId);
        } else {
            log.error("Ошибка при добавлении лайка");
            throw new NotFoundException("Фильм с " + filmId + " отсутствует.");
        }
    }

    public void removeLike(Long userId, Long filmId) {
        Optional<User> user = inMemoryUserStorage.getUserById(userId);
        if (user.isEmpty()) {
            log.error("Ошибка при удалении из друзей");
            throw new NotFoundException("Юзер с id " + userId + " отсутствует");
        }
        Optional<Film> film0 = inMemoryFilmStorage.getFilmById(filmId);
        if (film0.isPresent()) {
            film0.get().removeLike(userId);
            log.trace("Фильму с Id {} удален лайк", filmId);
        } else {
            log.error("Ошибка при удалении лайка");
            throw new NotFoundException("Фильм с " + filmId + " отсутствует.");
        }
    }

    public List<Film> getMostPopularFilms(Integer count) {
        List<Film> allFilmsList = new ArrayList<>(inMemoryFilmStorage.findAll());
        int numberToRemove = allFilmsList.size() - count;
        Collections.sort(allFilmsList);
        for (int i = 0; i < numberToRemove; i++) {
             allFilmsList.removeLast();
       }
        return allFilmsList;
    }
}
