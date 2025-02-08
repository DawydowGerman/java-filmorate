package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenresController {
    private final FilmService filmService;

    @Autowired
    public GenresController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{genreId}")
    public GenreDTO getGenreById(@PathVariable Long genreId) {
        return filmService.getGenreById(genreId);
    }

    @GetMapping
    public List<GenreDTO> findAll() {
        return filmService.getAllGenres();
    }
}