package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.dto.MpaDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private MpaStorage mpaStorage;
    private GenreStorage genreStorage;
    private final DatabaseFilmGenresStorage databaseFilmGenresStorage;
    private final DatabaseFilmDirectorsStorage databaseFilmDirectorsStorage;
    private final LikesStorage likesStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmService(@Qualifier("DatabaseFilmStorage") FilmStorage filmStorage,
                       @Qualifier("DatabaseUserStorage") UserStorage userStorage,
                       @Qualifier("DatabaseMpaStorage") MpaStorage mpaStorage,
                       @Qualifier("DatabaseGenreStorage") GenreStorage genreStorage,
                       DatabaseFilmGenresStorage databaseFilmGenresStorage,
                       DatabaseFilmDirectorsStorage databaseFilmDirectorsStorage,
                       @Qualifier("DatabaseLikesStorage") LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.databaseFilmGenresStorage = databaseFilmGenresStorage;
        this.databaseFilmDirectorsStorage = databaseFilmDirectorsStorage;
        this.likesStorage = likesStorage;
    }

    public FilmDTO create(FilmDTO filmDTO) {
        if (filmDTO.getName() == null || filmDTO.getName().isBlank()) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Название не может быть пустым");
        }
        if (filmDTO.getDescription() == null || filmDTO.getDescription().isBlank()) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Описание не может быть пустым");
        } else if (filmDTO.getDescription().length() > 200) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (filmDTO.getReleaseDate() == null) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Дата релиза должна быть указана");
        } else if (filmDTO.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (filmDTO.getDuration() == null) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Продолжительность фильма должна быть указана");
        } else if (filmDTO.getDuration() < 0) {
            log.error("Ошибка при добавлении фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        if (!mpaStorage.exists(filmDTO.getMpa().getId())) {
            log.error("Ошибка при добавлении фильма");
            throw new NotFoundException("MPA рэйтинг не найден с id = " + filmDTO.getMpa().getId());
        }
        for (Genre genre : filmDTO.getGenres()) {
            if (!genreStorage.exists(genre.getId())) {
                log.error("Ошибка при добавлении фильма");
                throw new NotFoundException("Жанр не найден с id = " + genre.getId());
            }
        }
        filmDTO.setGenres(filmDTO.getGenres().stream().distinct().toList());
        filmDTO.setDirectors(filmDTO.getDirectors().stream().distinct().toList());

        Film film = FilmMapper.toModel(filmDTO);
        film = filmStorage.create(film);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            databaseFilmGenresStorage.saveFilmGenres(film);
        }

        if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
            databaseFilmDirectorsStorage.saveFilmDirectors(film);
        }

        return FilmMapper.toDto(film);
    }

    public List<FilmDTO> findAll() {
        Optional<List<Film>> filmList = filmStorage.findAll();

        if (filmList.isEmpty()) {
            throw new NotFoundException("Список фильмов пуст.");
        }

        filmList.get()
                .stream()
                .forEach(film -> {
                    if (databaseFilmGenresStorage.isFilmHasGenre(film.getId())) {
                        this.assignGenres(film);
                    }
                    if (databaseFilmDirectorsStorage.isFilmHasDirector(film.getId())) {
                        this.assignDirectors(film);
                    }
                    this.assignMpa(film);
                    FilmMapper.toDto(film);
                });

        return filmList.get()
                .stream()
                .map(FilmMapper::toDto)
                .toList();
    }

    public FilmDTO getFilmById(Long filmId) {
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if (film.isEmpty()) {
            throw new NotFoundException("Фильм с " + filmId + " отсутствует.");
        }

        if (databaseFilmGenresStorage.isFilmHasGenre(film.get().getId())) {
            this.assignGenres(film.get());
        }

        if (databaseFilmDirectorsStorage.isFilmHasDirector(film.get().getId())) {
            this.assignDirectors(film.get());
        }

        this.assignMpa(film.get());
        return FilmMapper.toDto(film.get());
    }

    public FilmDTO update(FilmDTO filmDTO) {
        if (filmDTO.getId() == null) {
            log.error("Ошибка при обновлении фильма");
            throw new ValidationException("Id должен быть указан");
        }

        if (filmStorage.isFilmIdExists(filmDTO.getId())) {
            if (filmDTO.getName() == null || filmDTO.getName().isBlank()) {
                log.error("Ошибка при обновлении фильма");
                throw new ValidationException("Название не может быть пустым");
            }
            if (filmDTO.getDescription() == null || filmDTO.getDescription().isBlank()) {
                log.error("Ошибка при обновлении фильма");
                throw new ValidationException("Описание не может быть пустым");
            } else if (filmDTO.getDescription().length() > 200) {
                log.error("Ошибка при обновлении фильма");
                throw new ValidationException("Максимальная длина описания — 200 символов");
            }
            if (filmDTO.getReleaseDate() == null) {
                log.error("Ошибка при обновлении фильма");
                throw new ValidationException("Дата релиза должна быть указана");
            } else if (filmDTO.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error("Ошибка при обновлении фильма");
                throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
            }
            if (filmDTO.getDuration() == null) {
                log.error("Ошибка при добавлении фильма");
                throw new ValidationException("Продолжительность фильма должна быть указана");
            } else if (filmDTO.getDuration() < 0) {
                log.error("Ошибка при добавлении фильма");
                throw new ValidationException("Продолжительность фильма должна быть положительным числом");
            }
            if (!mpaStorage.exists(filmDTO.getMpa().getId())) {
                log.error("Ошибка при добавлении фильма");
                throw new ValidationException("MPA рэйтинг не найден с id = " + filmDTO.getMpa().getId());
            }
            for (Genre genre : filmDTO.getGenres()) {
                if (!genreStorage.exists(genre.getId())) {
                    log.error("Ошибка при добавлении фильма");
                    throw new NotFoundException("Жанр не найден с id = " + genre.getId());
                }
            }
            Film film = FilmMapper.toModel(filmDTO);
            film = filmStorage.update(film);

            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                databaseFilmGenresStorage.saveFilmGenres(film);
            }

            if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
                databaseFilmDirectorsStorage.saveFilmDirectors(film);
            }

            return FilmMapper.toDto(film);
        } else {
            log.error("Ошибка при обновлении фильма");
            throw new NotFoundException("Фильм с id = " + filmDTO.getId() + " не найден");
        }
    }

    public void remove(Long id) {
        if (!filmStorage.isFilmIdExists(id)) {
            log.error("Ошибка при удалении фильма с id = {}", id);
            throw new NotFoundException("Фильм не найден с id = " + id);
        }
        filmStorage.remove(id);
    }

    public void giveLike(Long userId, Long filmId) {
        if (userStorage.isUserIdExists(userId) && filmStorage.isFilmIdExists(filmId)) {
            likesStorage.giveLike(userId, filmId);
            log.trace("Фильму с Id {} поставлен лайк", filmId);

            return;
        }

        log.error("Ошибка при добавлении лайка");
        throw new NotFoundException("Фильм с id" + filmId + " либо юзер с id " + userId + " отсутствует.");
    }

    public void removeLike(Long userId, Long filmId) {
        if (userStorage.isUserIdExists(userId) && filmStorage.isFilmIdExists(filmId)) {
            likesStorage.removeLike(userId, filmId);
            log.trace("Для фильма с Id {} удален лайк", filmId);

            return;
        }

        log.error("Ошибка при удаленнии лайка");
        throw new NotFoundException("Фильм с id" + filmId + " либо юзер с id " + userId + " отсутствует.");
    }

    public List<FilmDTO> getMostPopularFilms(
            Integer count,
            Integer genderId,
            Integer year
    ) {
        if (filmStorage.findAll().isEmpty()) {
            log.error("Ошибка при получении списка самых популярных фильмов.");
            throw new NotFoundException("Список фильмов пуст.");
        }

        return filmStorage.getMostPopularFilms(count, genderId, year)
                .stream()
                .map(FilmMapper::toDto)
                .toList();
    }

    public List<FilmDTO> getCommonFilms(Long userId, Long friendId) {
        if (!userStorage.isUserIdExists(userId)) {
            throw new NotFoundException("Пользователь не найден с id = " + userId);
        }
        if (!userStorage.isUserIdExists(friendId)) {
            throw new NotFoundException("Пользователь не найден с id = " + friendId);
        }

        List<Film> commonFilms = filmStorage.getCommonFilms(userId, friendId);
        commonFilms.forEach(film -> {
            if (databaseFilmGenresStorage.isFilmHasGenre(film.getId())) {
                this.assignGenres(film);
            }
            this.assignMpa(film);
            FilmMapper.toDto(film);
        });

        return commonFilms
                .stream()
                .map(FilmMapper::toDto)
                .collect(Collectors.toList());
    }

    public MpaDTO getMpaById(Long mpaId) {
        return MpaMapper.toDTO(mpaStorage.getById(mpaId)
                .orElseThrow(() -> new NotFoundException("Mpa не найден с id = " + mpaId)));
    }

    public List<MpaDTO> getAllMpa() {
        List<Mpa> mpaList = mpaStorage.findAll();
        return mpaList
                .stream()
                .map(MpaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public GenreDTO getGenreById(Long genreId) {
        return GenreMapper.toDto(genreStorage.getById(genreId)
                .orElseThrow(() -> new NotFoundException("Жанр с " + genreId + " отсутствует.")));
    }

    public List<GenreDTO> getAllGenres() {
        List<Genre> genreList = genreStorage.findAll();
        return genreList
                .stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<FilmDTO> getFilmsByDirector(final Long directorId, final String sort) {
        return filmStorage.getFilmsByDirector(directorId, sort.equals("likes") ? "likes" : "year")
                .stream()
                .map(film -> {
                    this.assignGenres(film);
                    this.assignDirectors(film);
                    this.assignMpa(film);

                    return FilmMapper.toDto(film);
                })
                .toList();
    }

    private Film assignGenres(Film film) {
        List<Long> genresList = databaseFilmGenresStorage.getGenresIdsOfFilm(film.getId());
        List<Genre> filmGenresList = genreStorage.findAll()
                .stream()
                .filter(genre -> genresList.contains(genre.getId()))
                .toList();
        film.setGenres(filmGenresList);
        return film;
    }

    private Film assignMpa(Film film) {
        Optional<Mpa> optionalMpa = mpaStorage.getById(film.getMpa().getId());
        optionalMpa.ifPresent(mpa -> film.getMpa().setName(mpa.getName()));

        return film;
    }

    private void assignDirectors(Film film) {
        film.setDirectors(databaseFilmDirectorsStorage.getDirectorOfFilm(film.getId()));
    }
}