package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.Instant;
import java.time.Duration;

@SpringBootTest
class FilmTest {
    Film film0;
    Film film1;
    Film film2;

    @BeforeEach
    public void beforeEach() {
        film0 = Film.builder()
                .id(Long.valueOf(23))
                .name("film0")
                .description("some desc0")
                .releaseDate(Instant.parse("2022-12-28T00:00:00.00Z"))
                .duration(Duration.ofMinutes(120))
                .build();
        film1 = Film.builder()
                .id(Long.valueOf(44))
                .name("film1")
                .description("some desc1")
                .releaseDate(Instant.parse("2012-12-28T00:00:00.00Z"))
                .duration(Duration.ofMinutes(134))
                .build();
        film2 = Film.builder()
                .id(Long.valueOf(44))
                .name("film1")
                .description("some desc1")
                .releaseDate(Instant.parse("2012-12-28T00:00:00.00Z"))
                .duration(Duration.ofMinutes(134))
                .build();
    }

    @Test
    void getIdMethodTest() {
        Assertions.assertEquals(film0.getId(), Long.valueOf(23));
    }

    @Test
    void setIdMethodTest() {
        film0.setId(Long.valueOf(46));
        Assertions.assertEquals(film0.getId(), Long.valueOf(46));
    }

    @Test
    void getNameMethodTest() {
        Assertions.assertEquals(film0.getName(), "film0");
    }

    @Test
    void setNameMethodTest() {
        film0.setName("anotherName");
        Assertions.assertEquals(film0.getName(), "anotherName");
    }

    @Test
    void getDescriptionMethodTest() {
        Assertions.assertEquals(film0.getDescription(), "some desc0");
    }

    @Test
    void setDescriptionMethodTest() {
        film0.setDescription("another desc");
        Assertions.assertEquals(film0.getDescription(), "another desc");
    }

    @Test
    void getReleaseDateMethodTest() {
        Assertions.assertEquals(film0.getReleaseDate(), Instant.parse("2022-12-28T00:00:00.00Z"));
    }

    @Test
    void setReleaseDateMethodTest() {
        film0.setReleaseDate(Instant.parse("1999-12-28T00:00:00.00Z"));
        Assertions.assertEquals(film0.getReleaseDate(), Instant.parse("1999-12-28T00:00:00.00Z"));
    }

    @Test
    void getDurationDateMethodTest() {
        Assertions.assertEquals(film0.getDuration(), Duration.ofMinutes(120));
    }

    @Test
    void setDurationDateMethodTest() {
        film0.setDuration(Duration.ofMinutes(160));
        Assertions.assertEquals(film0.getDuration(), Duration.ofMinutes(160));
    }

    @Test
    void toStringMethodTest() {
        Assertions.assertEquals(film1.toString(),"Film(id=44, name=film1, description=some desc1, releaseDate=2012-12-28T00:00:00Z, duration=PT2H14M)");
    }

    @Test
    void equalsMethodTest() {
        Assertions.assertEquals(film1.equals(film2), true);
    }

    @Test
    void hashCodeMethodTest() {
        Assertions.assertEquals(film1.hashCode(), film2.hashCode());
    }
}
