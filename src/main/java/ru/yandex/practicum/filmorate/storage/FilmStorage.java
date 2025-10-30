package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<List<Film>> findAll();

    Film create(Film film);

    Film update(Film newFilm);

    void remove(Long id);

    Optional<Film> getFilmById(Long id);

    Boolean isFilmIdExists(Long id);

    List<Film> getMostPopularFilms(Integer limit, Integer genreId, Integer year);

    List<Film> getCommonFilms(Long userId, Long friendId);

    Optional<List<Film>> getRecommendations(Long id);

    Optional<List<Film>> getFilmsByDirector(final Long directorId, final String sort);

    List<Film> findByDirectorName(String query);

    List<Film> findByFilmTitle(String query);

   List<Film> getMostPopularByDirectorOrTitle(String query);

    Boolean isFilmTitleExists(String name);
}
