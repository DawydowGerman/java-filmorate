package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FilmControllerTest {
    FilmController filmController = new FilmController();

    Film film0 = Film.builder()
            .id(Long.valueOf(23))
            .name("film0")
            .description("some desc0")
            .releaseDate(LocalDate.of(2022, 12,28))
            .duration(Integer.valueOf(120))
            .build();

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
}
