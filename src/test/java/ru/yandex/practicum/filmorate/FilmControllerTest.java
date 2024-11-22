package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FilmControllerTest {
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(inMemoryFilmStorage);
    FilmController filmController = new FilmController(inMemoryFilmStorage, filmService);

    Film film0 = Film.builder()
            .id(Long.valueOf(23))
            .name("film0")
            .description("some desc0")
            .releaseDate(LocalDate.of(2022, 12,28))
            .duration(Integer.valueOf(120))
            .build();

    Film film00;
    Film film1;
    Film film2;
    Film film3;
    Film film4;
    Film film5;
    Film film6;
    Film film7;
    Film film8;
    Film film9;
    Film film10;
    Film film11;

    @BeforeEach
    public void beforeEach() {
        film00 = Film.builder()
                .id(Long.valueOf(0))
                .build();

        film1 = Film.builder()
                .id(Long.valueOf(1))
                .build();

        film2 = Film.builder()
                .id(Long.valueOf(2))
                .build();

        film3 = Film.builder()
                .id(Long.valueOf(3))
                .build();

        film4 = Film.builder()
                .id(Long.valueOf(4))
                .build();

        film5 = Film.builder()
                .id(Long.valueOf(5))
                .build();

        film6 = Film.builder()
                .id(Long.valueOf(6))
                .build();

        film7 = Film.builder()
                .id(Long.valueOf(7))
                .build();

        film8 = Film.builder()
                .id(Long.valueOf(8))
                .build();

        film9 = Film.builder()
                .id(Long.valueOf(9))
                .build();

        film10 = Film.builder()
                .id(Long.valueOf(10))
                .build();

        film11 = Film.builder()
                .id(Long.valueOf(11))
                .build();
    }

    @Test
    public void testFindAllMethodWithEmptyFilmsMap() throws Exception {
        try {
            filmController.findAll();
        } catch (NotFoundException e) {
            assertEquals(e.getMessage(), "Список фильмов пуст");
        }
    }

    @Test
    public void testFindAllMethodWithFilledMap() throws Exception {
        filmController.create(film0);
        assertEquals(filmController.findAll().size(), 1);
    }

    @Test
    public void testCreateMethodWithValidObject() throws Exception {
        Film testFilmObj = filmController.create(film0);
        assertEquals(testFilmObj, film0);
    }

    @Test
    public void testCreateMethodWhenNameNull() throws Exception {
        film0.setName(null);
        try {
            filmController.create(film0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Название не может быть пустым");
        }
    }

    @Test
    public void testCreateMethodWhenDescNull() throws Exception {
        film0.setDescription(null);
        try {
            filmController.create(film0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Описание не может быть пустым");
        }
    }

    @Test
    public void testCreateMethodWhenDescMoreThan200Chars() throws Exception {
        String desc = "1".repeat(201);;
        film0.setDescription(desc);
        try {

            filmController.create(film0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Максимальная длина описания — 200 символов");
        }
    }

    @Test
    public void testCreateMethodWhenReleaseDateNull() throws Exception {
        film0.setReleaseDate(null);
        try {
            filmController.create(film0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата релиза должна быть указана");
        }
    }

    @Test
    public void testCreateMethodWhenReleaseDateBefore18951228() throws Exception {
        film0.setReleaseDate(LocalDate.of(1894,12,28));
        try {
            filmController.create(film0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата релиза — не раньше 28 декабря 1895 года");
        }
    }

    @Test
    public void testCreateMethodWithNullDuration() throws Exception {
        film0.setDuration(null);
        try {
            filmController.create(film0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Продолжительность фильма должна быть указана");
        }
    }

    @Test
    public void testCreateMethodWithNegativeDuration() throws Exception {
        film0.setDuration(Integer.valueOf(-120));
        try {
            filmController.create(film0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Продолжительность фильма должна быть положительным числом");
        }
    }

    @Test
    public void testUpdateMethodWithNullId() throws Exception {
        filmController.create(film0);
        film0.setId(null);
        try {
            filmController.update(film0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Id должен быть указан");
        }

    }

    @Test
    public void testUpdateMethodWithSameName() throws Exception {
        filmController.create(film0);
        Film film1 = film0;
        film1.setName("another one");
        try {
            filmController.update(film1);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Название фильма не может быть изменено");
        }
    }

    @Test
    public void testUpdateMethodWithOtherReleaseDate() throws Exception {
        filmController.create(film0);
        Film film1 = film0;
        film1.setReleaseDate(LocalDate.of(2022,12,21));
        try {
            filmController.update(film1);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата релиза не может быть изменена");
        }
    }

    @Test
    public void testUpdateMethodWithOtherDurationDate() throws Exception {
        filmController.create(film0);
        Film film1 = film0;
        film1.setDuration(Integer.valueOf(119));
        try {
            filmController.update(film1);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Продолжительность фильма не может быть изменена");
        }
    }

    @Test
    public void testUpdateMethodWithValidRequest() throws Exception {
        filmController.create(film0);
        Film film1 = (film0);
        film1.setDescription("other desc");
        filmController.update(film1);
        assertEquals(film1.getDescription(), "other desc");
    }

    @Test
    public void testUpdateMethodWithWrongId() throws Exception {
        filmController.create(film0);
        Film film1 = (film0);
        film1.setId(Long.valueOf(44));
        try {
            filmController.update(film1);
        } catch (NotFoundException e) {
            assertEquals(e.getMessage(), "Фильм с id = 44 не найден");
        }
    }

    @Test
    public void giveLikeMethodWithWrongId() throws Exception {
        inMemoryFilmStorage.create(film0);
        filmController.giveLike(Long.valueOf(01), film0.getId());
        assertEquals(film0.getLikes().size(), 1);
    }

    @Test
    public void removeLikeMethodWithWrongId() throws Exception {
        inMemoryFilmStorage.create(film0);
        filmController.giveLike(Long.valueOf(01), film0.getId());
        filmController.removeLike(Long.valueOf(01), film0.getId());
        assertEquals(film0.getLikes().size(), 0);
    }

    @Test
    public void getFilmByIdMethodWithWrongId() throws Exception {
        inMemoryFilmStorage.create(film0);
        assertEquals(filmController.getFilmById(film0.getId()), film0);
    }

    @Test
    public void getMostPopularFilmsMethodTest() throws Exception {
        film00.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0))));
        inMemoryFilmStorage.create(film00);

        film1.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0),
                Long.valueOf(1))));
        inMemoryFilmStorage.create(film1);

        film2.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0),
                Long.valueOf(1),
                Long.valueOf(2))));
        inMemoryFilmStorage.create(film2);

        film3.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0),
                Long.valueOf(1),
                Long.valueOf(2),
                Long.valueOf(3))));
        inMemoryFilmStorage.create(film3);

        film4.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0),
                Long.valueOf(1),
                Long.valueOf(2),
                Long.valueOf(3),
                Long.valueOf(4))));
        inMemoryFilmStorage.create(film4);

        film5.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0),
                Long.valueOf(1),
                Long.valueOf(2),
                Long.valueOf(3),
                Long.valueOf(4),
                Long.valueOf(5))));
        inMemoryFilmStorage.create(film5);

        film6.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0),
                Long.valueOf(1),
                Long.valueOf(2),
                Long.valueOf(3),
                Long.valueOf(4),
                Long.valueOf(5),
                Long.valueOf(6))));
        inMemoryFilmStorage.create(film6);

        film7.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0),
                Long.valueOf(1),
                Long.valueOf(2),
                Long.valueOf(3),
                Long.valueOf(4),
                Long.valueOf(5),
                Long.valueOf(6),
                Long.valueOf(7))));
        inMemoryFilmStorage.create(film7);

        film8.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0),
                Long.valueOf(1),
                Long.valueOf(2),
                Long.valueOf(3),
                Long.valueOf(4),
                Long.valueOf(5),
                Long.valueOf(6),
                Long.valueOf(7),
                Long.valueOf(8))));
        inMemoryFilmStorage.create(film8);

        film9.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0),
                Long.valueOf(1),
                Long.valueOf(2),
                Long.valueOf(3),
                Long.valueOf(4),
                Long.valueOf(5),
                Long.valueOf(6),
                Long.valueOf(7),
                Long.valueOf(8),
                Long.valueOf(9))));
        inMemoryFilmStorage.create(film9);

        film10.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0),
                Long.valueOf(1),
                Long.valueOf(2),
                Long.valueOf(3),
                Long.valueOf(4),
                Long.valueOf(5),
                Long.valueOf(6),
                Long.valueOf(7),
                Long.valueOf(8),
                Long.valueOf(9),
                Long.valueOf(10))));
        inMemoryFilmStorage.create(film10);

        film11.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0),
                Long.valueOf(1),
                Long.valueOf(2),
                Long.valueOf(3),
                Long.valueOf(4),
                Long.valueOf(5),
                Long.valueOf(6),
                Long.valueOf(7),
                Long.valueOf(8),
                Long.valueOf(9),
                Long.valueOf(10),
                Long.valueOf(11))));
        inMemoryFilmStorage.create(film11);

        List<Film> filmsList = filmController.getMostPopularFilms(Optional.of(10));

        assertEquals(filmsList.contains(film00), false);
        assertEquals(filmsList.contains(film1), false);
        assertEquals(filmsList.contains(film2), true);
    }
}
