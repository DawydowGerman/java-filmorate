package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("Добавлен фильм с Id {}", film.getId());
        return film;
    }

    @Override
    public Optional<List<Film>> findAll() {
        if (films.isEmpty()) {
            log.error("Ошибка при получении списка фильмов");
            return Optional.empty();
        } else return Optional.of((List<Film>) films.values());
    }

    @Override
    public List<Film> findByFilmTitle(String query) {
        String lowerCaseQuery = query.toLowerCase().trim();

        return films.values()
                .stream()
                .filter(f -> f.getName() != null &&
                        f.getName().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> getMostPopularByDirectorOrTitle(String query) {
        String lowerCaseQuery = query.toLowerCase().trim();
        return films.values()
                .stream()
                .filter(f -> (f.getName() != null && f.getName().toLowerCase().contains(lowerCaseQuery)) ||
                        (f.getDirectors() != null && f.getDirectors().stream()
                                .anyMatch(d -> d.getName() != null &&
                                        d.getName().toLowerCase().contains(lowerCaseQuery))))
                .sorted(Comparator.comparing(f -> f.getLikes().size(), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public Optional<Film> getFilmById(Long filmId) {
        if (films.size() == 0 || !films.containsKey(filmId)) {
            log.error("Список фильмов пусть либо фильм с id " + filmId + " не найден.");
            return Optional.empty();
        }
        return Optional.of(films.get(filmId));
    }

    public Film update(Film newFilm) {
        Long newFilmId = newFilm.getId();
        if (films.size() == 0 || !films.containsKey(newFilmId)) {
            log.error("Список фильмов пусть либо фильм с id " + newFilmId + " не найден.");
            return null;
        }
        Film oldFilm = films.get(newFilmId);
        if (newFilm.getName() != null && !newFilm.getName().isEmpty()) {
            log.trace("Изменено название фильма с Id {}", newFilmId);
            oldFilm.setName(newFilm.getName());
        }
        if (newFilm.getDescription() != null && !newFilm.getDescription().isEmpty()) {
            log.trace("Изменено описание фильма с Id {}", newFilmId);
            oldFilm.setDescription(newFilm.getDescription());
        }
        if (newFilm.getReleaseDate() != null) {
            log.trace("Изменена дата релиза фильма с Id {}", newFilmId);
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }
        if (newFilm.getDuration() != null) {
            log.trace("Изменена дата продолительность фильма с Id {}", newFilmId);
            oldFilm.setDuration(newFilm.getDuration());
        }
        if (newFilm.getMpa() != null) {
            log.trace("Изменен MPA рэйтинг фильма с Id {}", newFilmId);
            oldFilm.setMpa(newFilm.getMpa());
        }
        if (newFilm.getGenres() != null) {
            log.trace("Изменены жанры фильма с Id {}", newFilmId);
            oldFilm.setGenres(newFilm.getGenres());
        }
        log.debug("Обновлен фильм с Id {}", newFilmId);
        return oldFilm;
    }

    @Override
    public void remove(Long id) {
        films.remove(id);
        log.debug("Фильм с id = {} удален", id);
    }

    @Override
    public List<Film> getMostPopularFilms(Integer limit, Integer genreId, Integer year) {
        List<Film> allFilmsList = this.findAll().get();
        Collections.sort(allFilmsList);
        return allFilmsList;
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        List<Film> userFilms = films.values()
                .stream()
                .filter(film -> film.getLikes().contains(userId))
                .toList();
        List<Film> friendFilms = films.values()
                .stream()
                .filter(film -> film.getLikes().contains(friendId))
                .toList();

        List<Film> commonFilms = new ArrayList<>(userFilms);
        commonFilms.retainAll(friendFilms);
        Collections.sort(commonFilms);

        return commonFilms;
    }

    @Override
    public Optional<List<Film>> getRecommendations(Long id) {
        List<Film> listFromMap = new ArrayList<>(films.values());
        List<Long> listUsers = new ArrayList<>();
        List<Film> listOfFilmsLikedByOtherUsers = new ArrayList<>();
        List<Film> filmsLikedByUser = new ArrayList<>(films.values()).stream()
                .filter(film -> film.getLikes().contains(id))
                .collect(Collectors.toList());
        for (Film film0 : filmsLikedByUser) {
            for (Long long1 : film0.getLikes()) {
                if (!long1.equals(id)) {
                    listUsers.add(long1);
                }
            }
        }
        for (Film film1 : listFromMap) {
            for (Long long2 : film1.getLikes()) {
                for (Long long3 : listUsers) {
                    if (long3.equals(long2)) {
                        listOfFilmsLikedByOtherUsers.add(film1);
                    }
                }
            }
        }
        listOfFilmsLikedByOtherUsers.removeAll(filmsLikedByUser);
        if (listOfFilmsLikedByOtherUsers.size() != 0 || listOfFilmsLikedByOtherUsers != null) {
            return Optional.of(listOfFilmsLikedByOtherUsers);
        }
        return Optional.empty();
    }

    @Override
    public Boolean isFilmIdExists(Long id) {
        return films.containsKey(id);
    }

    @Override
    public Optional<List<Film>> getFilmsByDirector(final Long directorId, final String sort) {
        return Optional.empty();
    }

    @Override
    public List<Film> findByDirectorName(String query) {
        String lowerCaseQuery = query.toLowerCase().trim();
        return films.values()
                .stream()
                .filter(f -> f.getDirectors() != null && f.getDirectors().stream()
                        .anyMatch(d -> d.getName() != null &&
                        d.getName().toLowerCase().contains(lowerCaseQuery)))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean isFilmTitleExists(String name) {
        return films.values().stream()
                .anyMatch(film -> name.equals(film.getName()));
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
