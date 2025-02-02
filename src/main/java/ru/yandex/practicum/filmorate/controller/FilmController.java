package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;

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

    @PutMapping("/{id}/like/{userId}")
    public void giveLike(@PathVariable Long userId, @PathVariable Long id) {
        filmService.giveLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long userId, @PathVariable Long id) {
        filmService.removeLike(userId, id);
    }

    @GetMapping("/popular")
    public List<FilmDTO> getMostPopularFilms(@RequestParam(required = false) Optional<Integer> count) {
        if (count.isPresent()) {
            return filmService.getMostPopularFilms(count.get());
        }
        return filmService.getMostPopularFilms(Integer.valueOf(10));
    }
}