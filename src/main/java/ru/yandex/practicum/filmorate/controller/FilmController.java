package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public FilmDTO create(@Valid @RequestBody FilmDTO filmDTO) {
        return filmService.create(filmDTO);
    }

    @GetMapping
    public List<FilmDTO> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public FilmDTO getFilmById(@PathVariable Long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PutMapping
    public FilmDTO update(@RequestBody FilmDTO filmDTO) {
        return filmService.update(filmDTO);
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable(name = "userId") Long id) {
        filmService.remove(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void giveLike(@PathVariable Long userId, @PathVariable Long id) {
        filmService.giveLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long userId, @PathVariable Long id) {
        filmService.removeLike(userId, id);
    }

    @GetMapping("/popular")
    public List<FilmDTO> getMostPopularFilms(
            @RequestParam(required = false, defaultValue = "10") Integer count,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) Integer year
    ) {
        return filmService.getMostPopularFilms(count, genreId, year);
    }

    @GetMapping("/common")
    public List<FilmDTO> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{id}")
    public List<FilmDTO> getDirectorFilms(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "year") String sortBy
    ) {
        return filmService.getFilmsByDirector(id, sortBy);
    }

    @GetMapping("/search")
    public List<FilmDTO> searchByTitleAndDirector(@RequestParam String query,
                                                  @RequestParam List<String> by) {
        return filmService.searchByTitleAndDirector(query, by);
    }
}
