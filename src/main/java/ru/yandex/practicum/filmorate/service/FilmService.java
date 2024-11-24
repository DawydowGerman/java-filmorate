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
import java.util.stream.Collectors;

import java.util.stream.*;

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
        Collections.sort(allFilmsList, (Film a1, Film a2) -> a1.getLikes().size()-a2.getLikes().size());
        return allFilmsList;

        /*
        User result = users.stream()
                .max(Comparator.comparingInt(film -> film.likes.size()))
                .orElse(null);

.stream()
                .max(Comparator.comparingInt(film -> film.getLikes().size()));
                .orElse(null);
            //    .collect(Collectors.toList());



        int allFilmsListSize = allFilmsList.size();
        List<Film> result = new ArrayList<>();
        Film fimlWithLargestLikes;
        for (int i = 0; i < count && i < allFilmsListSize; i++) {
            for (int j = 0; j < allFilmsList.size(); j++) {
                boolean arrayOfFilmsWithEmptyLikes = true;
                fimlWithLargestLikes = Film.builder().build();
                if (allFilmsList.get(j).getLikes().size() > fimlWithLargestLikes.getLikes().size()) {
                    fimlWithLargestLikes = allFilmsList.get(j);
                    arrayOfFilmsWithEmptyLikes = false;
                }
                if (j == allFilmsList.size() - 1 && arrayOfFilmsWithEmptyLikes) {
                    result.addAll(allFilmsList);
                    return result;
                }
                if (j == allFilmsList.size() - 1) {
                    allFilmsList.remove(fimlWithLargestLikes);
                    result.add(fimlWithLargestLikes);
                }
            }
        }
        log.trace("Возвращены наиболее популярные фильмы");
        return result;

         */
    }
}
