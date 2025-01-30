package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<List<Film>> findAll();

    Film create(Film film);

    Film update(Film newFilm);

    Optional<Film> getFilmById(Long id);

    public boolean isFilmIdExists(Long id);

    public List<Film> getMostPopularFilms();

    List<Film> getCommonFilms(Long userId, Long friendId);

    public Optional<List<Film>> getRecommendations(Long id);
}