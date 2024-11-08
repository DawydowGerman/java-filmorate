package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() throws Exception {
        Exception e;
        if (films.size() == 0) {
            e = new NotFoundException("Список фильмов пуст");
            log.error("Ошибка при добавлении фильма", e);
            throw e;
        } else return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws Exception {
        Exception e;
        if (film.getName() == null || film.getName().isBlank()) {
            e = new ValidationException("Название не может быть пустым");
            log.error("Ошибка при добавлении фильма", e);
            throw e;
        }
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            e = new ValidationException("Описание не может быть пустым");
            log.error("Ошибка при добавлении фильма", e);
            throw e;
        } else if (film.getDescription().length() > 200) {
            e = new ValidationException("Максимальная длина описания — 200 символов");
            log.error("Ошибка при добавлении фильма", e);
            throw e;
        }
        if (film.getReleaseDate() == null) {
            e = new ValidationException("Дата релиза должна быть указана");
            log.error("Ошибка при добавлении фильма", e);
            throw e;
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            e = new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
            log.error("Ошибка при добавлении фильма", e);
            throw e;
        }
        if (film.getDuration() == null) {
            e = new ValidationException("Продолжительность фильма должна быть указана");
            log.error("Ошибка при добавлении фильма", e);
            throw e;
        } else if (film.getDuration() < 0) {
            e = new ValidationException("Продолжительность фильма должна быть положительным числом");
            log.error("Ошибка при добавлении фильма", e);
            throw e;
        }
        if (film.getId() == null) {
            film.setId(getNextId());
        }
        films.put(film.getId(), film);
        log.debug("Добавлен фильм с Id {}", film.getId());
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) throws Exception {
        Exception e;
        if (newFilm.getId() == null) {
            e = new ValidationException("Id должен быть указан");
            log.error("Ошибка при добавлении фильма", e);
            throw e;
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (!newFilm.getName().equals(oldFilm.getName())) {
                e = new ValidationException("Название фильма не может быть изменено");
                log.error("Ошибка при добавлении фильма", e);
                throw e;
            }
            if (!newFilm.getReleaseDate().equals(oldFilm.getReleaseDate())) {
                e = new ValidationException("Дата релиза не может быть изменена");
                log.error("Ошибка при добавлении фильма", e);
                throw e;
            }
            if (!newFilm.getDuration().equals(oldFilm.getDuration())) {
                e = new ValidationException("Продолжительность фильма не может быть изменена");
                log.error("Ошибка при добавлении фильма", e);
                throw e;
            }
            if (newFilm.getDescription() != null && !newFilm.getDescription().isEmpty()) {
                log.trace("Изменено описание фильма с Id {}", newFilm.getId());
                oldFilm.setDescription(newFilm.getDescription());
            }
            log.debug("Обновлен фильм с Id {}", newFilm.getId());
            return oldFilm;
        }
        e = new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        log.error("Ошибка при добавлении фильма", e);
        throw e;
    }
}
