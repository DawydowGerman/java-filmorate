package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;
    private InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return inMemoryFilmStorage.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void giveLike(@PathVariable Long userId, @PathVariable Long id) {
        filmService.giveLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long userId, @PathVariable Long id) {
        filmService.removeLike(userId, id);
    }

    @GetMapping("/films/popular?count={count}")
    public List<Film> getMostPopularFilms(@PathVariable @RequestParam Optional<Integer> count) {
        if (count.isPresent()) {
            return filmService.getMostPopularFilms(count.get());
        }
        return filmService.getMostPopularFilms(Integer.valueOf(10));
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Long filmId) {
        Optional<Film> film0 = inMemoryFilmStorage.getFilmById(filmId);
        if (film0.isPresent()) {
            return film0.get();
        } else throw new NotFoundException("Фильм с " + filmId + " отсутствует.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowable(final Throwable e) {
        return Map.of(
            "errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        return Map.of("errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        return Map.of("errorMessage", e.getMessage());
    }
}
