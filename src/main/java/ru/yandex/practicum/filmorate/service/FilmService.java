package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DatabaseFilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@Data
public class FilmService {
    private FilmStorage filmStorage;
    private DatabaseFilmGenresStorage databaseFilmGenresStorage;
    private UserStorage userStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmService( @Qualifier("DatabaseFilmStorage")FilmStorage filmStorage,
                        @Qualifier("DatabaseUserStorage")UserStorage userStorage,
                        DatabaseFilmGenresStorage databaseFilmGenresStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.databaseFilmGenresStorage = databaseFilmGenresStorage;
    }

    public FilmDTO create(FilmDTO filmDTO) {

        /*
        // TEST WHAT TO COME IN create():
        System.out.println("filmDTO: 1)NAME: " +
                filmDTO.getName() +
                " 2)DESC: " + filmDTO.getDescription() +
                " 3)RELEASEDATE: " + filmDTO.getReleaseDate() +
                " 4)DURATION: " + filmDTO.getDuration() +
                " 5)MPA_ID " + filmDTO.getMpa().getId() +
                " 6)GENRES_SIZE: " + filmDTO.getGenres().size());
         */

        if (filmDTO.getName() == null || filmDTO.getName().isBlank()) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Название не может быть пустым");
        }
        if (filmDTO.getDescription() == null || filmDTO.getDescription().isBlank()) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Описание не может быть пустым");
        } else if (filmDTO.getDescription().length() > 200) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (filmDTO.getReleaseDate() == null) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Дата релиза должна быть указана");
        } else if (filmDTO.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (filmDTO.getDuration() == null) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Продолжительность фильма должна быть указана");
        } else if (filmDTO.getDuration() < 0) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        Film film = FilmMapper.toModel(filmDTO);
        film = filmStorage.create(film);
        if(film.getGenres() != null && film.getGenres().size() > 0) {
            databaseFilmGenresStorage.saveFilmGenres(film);
        }
        return FilmMapper.toDto(film);
    }








    public void giveLike(Long userId, Long filmId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            log.error("Ошибка при удалении из друзей");
            throw new NotFoundException("Юзер с id " + userId + " отсутствует");
        }
        Optional<Film> film0 = filmStorage.getFilmById(filmId);
        if (film0.isPresent()) {
            film0.get().addLike(userId);
            log.trace("Фильму с Id {} поставлен лайк", filmId);
        } else {
            log.error("Ошибка при добавлении лайка");
            throw new NotFoundException("Фильм с " + filmId + " отсутствует.");
        }
    }

    public void removeLike(Long userId, Long filmId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            log.error("Ошибка при удалении из друзей");
            throw new NotFoundException("Юзер с id " + userId + " отсутствует");
        }
        Optional<Film> film0 = filmStorage.getFilmById(filmId);
        if (film0.isPresent()) {
            film0.get().removeLike(userId);
            log.trace("Фильму с Id {} удален лайк", filmId);
        } else {
            log.error("Ошибка при удалении лайка");
            throw new NotFoundException("Фильм с " + filmId + " отсутствует.");
        }
    }

    public List<Film> getMostPopularFilms(Integer count) {
        List<Film> allFilmsList = new ArrayList<>(filmStorage.findAll());
        int numberToRemove = allFilmsList.size() - count;
        Collections.sort(allFilmsList);
        for (int i = 0; i < numberToRemove; i++) {
            allFilmsList.removeLast();
        }
        return allFilmsList;
    }
}