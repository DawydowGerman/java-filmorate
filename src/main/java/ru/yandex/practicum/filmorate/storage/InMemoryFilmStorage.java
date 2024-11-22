package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> findAll() {
        if (films.size() == 0) {
            log.error("Ошибка при получении списка фильма");
            return null;
        } else return films.values();
    }

    public Film create(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Описание не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate() == null) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Дата релиза должна быть указана");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == null) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Продолжительность фильма должна быть указана");
        } else if (film.getDuration() < 0) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
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

    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Ошибка при обновлении фильма");
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() != null && !newFilm.getName().isEmpty()) {
                log.trace("Изменено название фильма с Id {}", newFilm.getId());
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getReleaseDate() != null) {
                log.trace("Изменена дата релиза фильма с Id {}", newFilm.getId());
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            if (newFilm.getDuration() != null) {
                log.trace("Изменена дата продолительность фильма с Id {}", newFilm.getId());
                oldFilm.setDuration(newFilm.getDuration());
            }
            if (newFilm.getDescription() != null && !newFilm.getDescription().isEmpty()) {
                log.trace("Изменено описание фильма с Id {}", newFilm.getId());
                oldFilm.setDescription(newFilm.getDescription());
            }
            log.debug("Обновлен фильм с Id {}", newFilm.getId());
            return oldFilm;
        }
        log.error("Ошибка при обновлении фильма");
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    public Optional<Film> getFilmById(Long id) {
        if (films.size() == 0) {
            log.error("Ошибка при получении списка юзеров");
            return Optional.empty();
        }
        if (films.containsKey(id)) {
            return Optional.of(films.get(id));
        }
        log.error("Ошибка при получении списка юзеров");
        return Optional.empty();
    }
}
