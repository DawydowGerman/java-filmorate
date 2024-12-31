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
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DatabaseFilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.DatabaseLikesStorage;
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
    private DatabaseLikesStorage databaseLikesStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmService(@Qualifier("DatabaseFilmStorage")FilmStorage filmStorage,
                        @Qualifier("DatabaseUserStorage")UserStorage userStorage,
                        DatabaseFilmGenresStorage databaseFilmGenresStorage,
                        DatabaseLikesStorage databaseLikesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.databaseFilmGenresStorage = databaseFilmGenresStorage;
        this.databaseLikesStorage = databaseLikesStorage;
    }

    public FilmDTO create(FilmDTO filmDTO) {
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
        if (filmDTO.getMpa().getId() > 5) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("MPA рэйтинг не может быть больше 5");
        }
        for (Genre genre : filmDTO.getGenres()) {
            if (genre.getId() > 6) {
                log.error("Ошибка при добавлении фильма");
                throw new ValidationException("Жанр не может иметь ID больше 6");
            }
        }
        Film film = FilmMapper.toModel(filmDTO);
        film = filmStorage.create(film);
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            databaseFilmGenresStorage.saveFilmGenres(film);
        }
        return FilmMapper.toDto(film);
    }

    public List<Film> findAll() {
        Optional<List<Film>> filmList = filmStorage.findAll();
        if (filmList.isPresent()) {
            return filmList.get();
        } else throw new NotFoundException("Список фильмов пуст.");
    }

    public FilmDTO update(FilmDTO filmDTO) {
        if (filmDTO.getId() == null) {
            log.error("Ошибка при обновлении фильма");
            throw new ValidationException("Id должен быть указан");
        }
        if (filmStorage.isFilmIdExists(filmDTO.getId())) {
            if (filmDTO.getName() == null || filmDTO.getName().isBlank()) {
                log.error("Ошибка при обновлении фильма");
                throw new ValidationException("Название не может быть пустым");
            }
            if (filmDTO.getDescription() == null || filmDTO.getDescription().isBlank()) {
                log.error("Ошибка при обновлении фильма");
                throw new ValidationException("Описание не может быть пустым");
            } else if (filmDTO.getDescription().length() > 200) {
                log.error("Ошибка при обновлении фильма");
                throw new ValidationException("Максимальная длина описания — 200 символов");
            }
            if (filmDTO.getReleaseDate() == null) {
                log.error("Ошибка при обновлении фильма");
                throw new ValidationException("Дата релиза должна быть указана");
            } else if (filmDTO.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
                log.error("Ошибка при обновлении фильма");
                throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
            }
            if (filmDTO.getDuration() == null) {
                log.error("Ошибка при добавлении фильма");
                throw new ValidationException("Продолжительность фильма должна быть указана");
            } else if (filmDTO.getDuration() < 0) {
                log.error("Ошибка при добавлении фильма");
                throw new ValidationException("Продолжительность фильма должна быть положительным числом");
            }
            if (filmDTO.getMpa().getId() > 5) {
                log.error("Ошибка при добавлении фильма");
                throw new ValidationException("MPA рэйтинг не может быть больше 5");
            }
            for (Genre genre : filmDTO.getGenres()) {
                if (genre.getId() > 6) {
                    log.error("Ошибка при добавлении фильма");
                    throw new ValidationException("Жанр не может иметь ID больше 6");
                }
            }
            Film film = FilmMapper.toModel(filmDTO);
            film = filmStorage.update(film);
            if (film.getGenres() != null && film.getGenres().size() > 0) {
                databaseFilmGenresStorage.saveFilmGenres(film);
            }
            return FilmMapper.toDto(film);
        } else {
            log.error("Ошибка при обновлении фильма");
            throw new NotFoundException("Фильм с id = " + filmDTO.getId() + " не найден");
        }
    }

    public void giveLike(Long userId, Long filmId) {
        if (userStorage.isUserIdExists(userId) && filmStorage.isFilmIdExists(filmId)) {
            databaseLikesStorage.saveFilmLikes(userId, filmId);
            log.trace("Фильму с Id {} поставлен лайк", filmId);
        } else {
            log.error("Ошибка при добавлении лайка");
            throw new NotFoundException("Фильм с id" + filmId + " либо юзер с id " + userId + " отсутствует.");
        }
    }

    public void removeLike(Long userId, Long filmId) {
        if (userStorage.isUserIdExists(userId) && filmStorage.isFilmIdExists(filmId)) {
            databaseLikesStorage.removeFilmLikes(userId, filmId);
            log.trace("Для фильма с Id {} удален лайк", filmId);
        } else {
            log.error("Ошибка при удаленнии лайка");
            throw new NotFoundException("Фильм с id" + filmId + " либо юзер с id " + userId + " отсутствует.");
        }
    }

    public List<Film> getMostPopularFilms(Integer count) {
        if (filmStorage.findAll().isPresent()) {
            List<Film> allFilmsList = filmStorage.getMostPopularFilms();
            int numberToRemove = allFilmsList.size() - count;
            for (int i = 0; i < numberToRemove; i++) {
                allFilmsList.removeLast();
            }
            return allFilmsList;
        } else {
            log.error("Ошибка при удалении лайка");
            throw new NotFoundException("Список фильмов пуст");
        }
    }
}