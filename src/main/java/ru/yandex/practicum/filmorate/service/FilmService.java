package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class FilmService {
    private FilmStorage inMemoryFilmStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void giveLike(Long userId, Long filmId) {
        Optional<Film> film0 = inMemoryFilmStorage.getFilmById(filmId);
        if (film0.isPresent()) {
            film0.get().addLike(userId);
            log.trace("Фильму с Id {} поставлен лайк", filmId);
        } else {
            log.error("Ошибка при добавлении лайка");
            throw new NotFoundException("Фильм с " + filmId + " отсутствует.");
        }
    }

    public void removeLike(Long userId, Long filmId) {
        Optional<Film> film0 = inMemoryFilmStorage.getFilmById(filmId);
        if (film0.isPresent()) {
            film0.get().removeLike(userId);
            log.trace("Фильму с Id {} удален лайк", filmId);
        } else {
            log.error("Ошибка при удалении лайка");
            throw new NotFoundException("Фильм с " + filmId + " отсутствует.");
        }
    }

    public List<Film> getMostPopularFilms(Integer count) {
        List<Film> allFilmsList = new ArrayList<>(inMemoryFilmStorage.findAll());
        List<Film> result = new ArrayList<>();
        Film fimlWithLargestLikes;
        for (int i = 0; i < count && i < allFilmsList.size(); i++) {
            for (int j = 0; j < allFilmsList.size(); j++) {
                fimlWithLargestLikes = Film.builder().build();
                if (allFilmsList.get(j).getLikes().size() > fimlWithLargestLikes.getLikes().size()) {
                    fimlWithLargestLikes = allFilmsList.get(j);
                }
                if (j == allFilmsList.size() - 1) {
                    allFilmsList.remove(fimlWithLargestLikes);
                    result.add(fimlWithLargestLikes);
                }
            }
        }
        log.trace("Возвращены наиболее популярные фильмы");
        return result;
    }
}
