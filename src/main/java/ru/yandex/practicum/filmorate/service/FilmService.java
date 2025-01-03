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
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Long.valueOf;

@Service
@Data
public class FilmService {
    private FilmStorage filmStorage;
    private DatabaseFilmGenresStorage databaseFilmGenresStorage;
    private UserStorage userStorage;
    private DatabaseLikesStorage databaseLikesStorage;
    private DatabaseMpaStorage databaseMpaStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmService(@Qualifier("DatabaseFilmStorage")FilmStorage filmStorage,
                        @Qualifier("DatabaseUserStorage")UserStorage userStorage,
                        DatabaseFilmGenresStorage databaseFilmGenresStorage,
                        DatabaseLikesStorage databaseLikesStorage,
                        DatabaseMpaStorage databaseMpaStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.databaseFilmGenresStorage = databaseFilmGenresStorage;
        this.databaseLikesStorage = databaseLikesStorage;
        this.databaseMpaStorage = databaseMpaStorage;
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

    public Film getFilmById(Long filmId) {
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if (film.isPresent()) {
            if (databaseFilmGenresStorage.isFilmHasGenre(film.get().getId())) {
                List<Genre> filmGenresList = new ArrayList<>();
                List<Integer> genresList = databaseFilmGenresStorage.getGenresIdsOfFilm(film.get().getId());
                for(int i = 0; i < genresList.size(); i++) {
                     switch (genresList.get(i)) {
                         case 1:
                             filmGenresList.add(new Genre(valueOf(1), "Комедия"));
                             break;
                         case 2:
                             filmGenresList.add(new Genre(valueOf(2), "Драма"));
                             break;
                         case 3:
                             filmGenresList.add(new Genre(valueOf(3), "Мультфильм"));
                             break;
                         case 4:
                             filmGenresList.add(new Genre(valueOf(4), "Триллер"));
                             break;
                         case 5:
                             filmGenresList.add(new Genre(valueOf(5), "Документальный"));
                             break;
                         case 6:
                             filmGenresList.add(new Genre(valueOf(6), "Боевик"));
                             break;
                     }
                }
                film.get().setGenres(filmGenresList);
            }
            return film.get();
        } else throw new NotFoundException("Фильм с " + filmId + " отсутствует.");
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
            log.error("Ошибка при получении списка самых популярных фильмов.");
            throw new NotFoundException("Список фильмов пуст.");
        }
    }

    public Mpa getMpaById(Integer mpaId) {
        if (mpaId > 5 || mpaId < 1) {
            throw new NotFoundException("MPA с " + mpaId + " отсутствует.");
        }
        Mpa mpa = new Mpa();
        switch (mpaId) {
            case 1:
                mpa = new Mpa(valueOf(1), "G");
                break;
            case 2:
                mpa = new Mpa(valueOf(2), "PG");
                break;
            case 3:
                mpa = new Mpa(valueOf(3), "PG-13");
                break;
            case 4:
                mpa = new Mpa(valueOf(4), "R");
                break;
            case 5:
                mpa = new Mpa(valueOf(5), "NC-17");
                break;
        }
        return mpa;
    }

    public List<Mpa> getAllMpa() {
        List<Mpa> mpaList = new ArrayList<>();
        mpaList.add(new Mpa(valueOf(1), "G"));
        mpaList.add(new Mpa(valueOf(2), "PG"));
        mpaList.add(new Mpa(valueOf(3), "PG-13"));
        mpaList.add(new Mpa(valueOf(4), "R"));
        mpaList.add(new Mpa(valueOf(5), "NC-17"));
        return mpaList;
    }

    public Genre getGenreById(Integer genreId) {
        if (genreId > 6 || genreId < 1) {
            throw new NotFoundException("Жанр с " + genreId + " отсутствует.");
        }
        Genre genre = new Genre();
        switch (genreId) {
            case 1:
                genre = new Genre(valueOf(1), "Комедия");
                break;
            case 2:
                genre = new Genre(valueOf(2), "Драма");
                break;
            case 3:
                genre = new Genre(valueOf(3), "Мультфильм");
                break;
            case 4:
                genre = new Genre(valueOf(4), "Триллер");
                break;
            case 5:
                genre = new Genre(valueOf(5), "Документальный");
                break;
            case 6:
                genre = new Genre(valueOf(6), "Боевик");
                break;
        }
        return genre;
    }

    public List<Genre> getAllGenres() {
        List<Genre> genreList = new ArrayList<>();
        genreList.add(new Genre(valueOf(1), "Комедия"));
        genreList.add(new Genre(valueOf(2), "Драма"));
        genreList.add(new Genre(valueOf(3), "Мультфильм"));
        genreList.add(new Genre(valueOf(4), "Триллер"));
        genreList.add(new Genre(valueOf(5), "Документальный"));
        genreList.add(new Genre(valueOf(6), "Боевик"));
        return genreList;
    }
}