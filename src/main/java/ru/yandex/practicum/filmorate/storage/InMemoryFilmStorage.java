package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        if (film.getId() == null) {
            film.setId(getNextId());
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        films.put(film.getId(), film);
        log.debug("Добавлен фильм с Id {}", film.getId());
        return film;
    }

    @Override
    public Optional<List<Film>> findAll() {
        if (films.size() == 0) {
            log.error("Ошибка при получении списка фильмов");
            return Optional.empty();
        } else return Optional.of((List<Film>)films.values());
    }

    public Optional<Film> getFilmById(Long filmId) {
        if (films.size() == 0) {
            log.error("Ошибка при получении списка юзеров");
            return Optional.empty();
        }
        if (films.containsKey(filmId)) {
            return Optional.of(films.get(filmId));
        } else {
            log.error("Ошибка при получении списка юзеров");
            return Optional.empty();
        }
    }

    public Film update(Film newFilm) {
        Film oldFilm = films.get(newFilm.getId());
        if (newFilm.getName() != null && !newFilm.getName().isEmpty()) {
            log.trace("Изменено название фильма с Id {}", newFilm.getId());
            oldFilm.setName(newFilm.getName());
        }
        if (newFilm.getDescription() != null && !newFilm.getDescription().isEmpty()) {
            log.trace("Изменено описание фильма с Id {}", newFilm.getId());
            oldFilm.setDescription(newFilm.getDescription());
        }
        if (newFilm.getReleaseDate() != null) {
            log.trace("Изменена дата релиза фильма с Id {}", newFilm.getId());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }
        if (newFilm.getDuration() != null) {
            log.trace("Изменена дата продолительность фильма с Id {}", newFilm.getId());
            oldFilm.setDuration(newFilm.getDuration());
        }
        if (newFilm.getMpa() != null) {
            log.trace("Изменен MPA рэйтинг фильма с Id {}", newFilm.getId());
            oldFilm.setMpa(newFilm.getMpa());
        }
        if (newFilm.getGenres() != null) {
            log.trace("Изменены жанры фильма с Id {}", newFilm.getId());
            oldFilm.setGenres(newFilm.getGenres());
        }
        log.debug("Обновлен фильм с Id {}", newFilm.getId());
        return oldFilm;
    }

    @Override
    public List<Film> getMostPopularFilms() {
        List<Film> allFilmsList = this.findAll().get();
        Collections.sort(allFilmsList);
        return allFilmsList;
    }

    @Override
    public boolean isFilmIdExists(Long id) {
        return films.containsKey(id);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}