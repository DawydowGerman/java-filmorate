package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static java.lang.Long.valueOf;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DatabaseUserStorage.class,
        UserRowMapper.class,
        DatabaseFilmStorage.class,
        FilmRowMapper.class,
        DatabaseLikesStorage.class,
        DatabaseFilmGenresStorage.class,
        DatabaseFriendshipStorage.class})

class FilmoRateApplicationTests {
    private final DatabaseUserStorage userStorage;
    private final DatabaseFilmStorage filmStorage;
    private final DatabaseLikesStorage databaseLikesStorage;
    private final DatabaseFilmGenresStorage databaseFilmGenresStorage;
    private final DatabaseFriendshipStorage databaseFriendshipStorage;

    User user0 = User.builder()
            .id(Long.valueOf(23))
            .email("some@mail.ru")
            .login("somelogin")
            .name("some name")
            .birthday(LocalDate.of(2000, 12, 28))
            .build();

    User user1 = User.builder()
            .id(Long.valueOf(45))
            .email("some@mail.ru")
            .login("somelogin")
            .name("some name")
            .birthday(LocalDate.of(2000, 12, 28))
            .build();

    User user2 = User.builder()
            .id(Long.valueOf(19))
            .email("some@mail.ru")
            .login("somelogin")
            .name("some name")
            .birthday(LocalDate.of(2000, 12, 28))
            .build();

    Film film0 = Film.builder()
            .id(Long.valueOf(23))
            .name("film0")
            .description("some desc0")
            .releaseDate(LocalDate.of(2022, 12,28))
            .duration(Long.valueOf(120))
            .build();

    Film film1 = Film.builder()
            .id(Long.valueOf(98))
            .name("film1")
            .description("some desc0")
            .releaseDate(LocalDate.of(2022, 12,28))
            .duration(Long.valueOf(120))
            .build();

    Film film2 = Film.builder()
            .id(Long.valueOf(15))
            .name("film2")
            .description("some desc0")
            .releaseDate(LocalDate.of(2022, 12,28))
            .duration(Long.valueOf(120))
            .build();

    @Test
    public void testCreateUserStorage() {
        User testUser = userStorage.create(user0);
         assertEquals(userStorage.isUserIdExists(testUser.getId()), true);
    }

    @Test
    public void testGetUserById() {
        User testUser = userStorage.create(user0);
        Optional<User> userOptional = userStorage.getUserById(testUser.getId());
        userOptional.get().setId(Long.valueOf(21));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", Long.valueOf(21))
                );
    }

    @Test
    public void testFindAllUserStorage() {
        userStorage.create(user0);
        userStorage.create(user1);
        Optional<List<User>> userList0 = userStorage.findAll();
        assertEquals(userList0.get().size(), 2);
    }

    @Test
    public void testUpdateUserStorage() {
        user1.setName("updateMethodTestObjectName");
        User testUser = userStorage.update(user1);
        assertEquals(testUser.getName(),user1.getName());
    }

    @Test
    public void testRemove() {
        userStorage.create(user0);
        User testUser = userStorage.create(user1);
        userStorage.remove(testUser.getId());
        Optional<List<User>> userList0 = userStorage.findAll();
        assertEquals(userList0.get().size(), 1);
    }

    @Test
    public void testIsUserIdExists() {
        User testUser = userStorage.create( user0);
        assertEquals(userStorage.isUserIdExists(testUser.getId()), true);
    }

    @Test
    public void testCreateFilmStorage() {
        Film testFilm = filmStorage.create(film0);
        assertEquals(filmStorage.isFilmIdExists(testFilm.getId()), true);
    }

    @Test
    public void testGetFilmById() {
        Film testFilm = filmStorage.create(film0);
        Optional<Film> filmOptional = filmStorage.getFilmById(testFilm.getId());
        filmOptional.get().setId(Long.valueOf(21));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", Long.valueOf(21))
                );
    }

    @Test
    public void testFindAllFilmStorage() {
        filmStorage.create(film0);
        filmStorage.create(film1);
        Optional<List<Film>> filmList = filmStorage.findAll();
        assertEquals(filmList.get().size(), 2);
    }

    @Test
    public void testUpdateFilmStorage() {
        film1.setName("updateMethodTestObjectName");
        Film testFilm = filmStorage.update(film1);
        assertEquals(testFilm.getName(),film1.getName());
    }

    @Test
    public void testGetMostPopularFilms() {
        filmStorage.create(film0);
        filmStorage.create(film1);
        filmStorage.create(film2);
        databaseLikesStorage.giveLike(user0.getId(), film1.getId());
        databaseLikesStorage.giveLike(user0.getId(), film2.getId());
        databaseLikesStorage.giveLike(user1.getId(), film2.getId());
        List<Film> mostPopularFilmsList = filmStorage.getMostPopularFilms();
        assertEquals(mostPopularFilmsList.get(0).getName(), "film2");
        assertEquals(mostPopularFilmsList.get(1).getName(), "film1");
    }

    @Test
    public void testIsFilmIdExists() {
        Film testFilm = filmStorage.create(film0);
        assertEquals(filmStorage.isFilmIdExists(testFilm.getId()), true);
    }

    @Test
    public void testRemoveLike() {
        filmStorage.create(film0);
        filmStorage.create(film1);
        filmStorage.create(film2);
        databaseLikesStorage.giveLike(user0.getId(), film1.getId());
        databaseLikesStorage.giveLike(user0.getId(), film2.getId());
        databaseLikesStorage.giveLike(user1.getId(), film2.getId());
        databaseLikesStorage.removeLike(user0.getId(), film1.getId());
        List<Film> mostPopularFilmsList = filmStorage.getMostPopularFilms();
        assertEquals(mostPopularFilmsList.size(), 1);
    }

    @Test
    public void testSaveFilmGenresAndGetGenresIdsOfFilm() {
        Genre genre1 = new Genre(valueOf(1), "Комедия");
        Genre genre2 = new Genre(valueOf(2), "Драма");
        List<Genre> genresList = new ArrayList<>();
        genresList.add(genre1);
        genresList.add(genre2);
        film0.setGenres(genresList);
        databaseFilmGenresStorage.saveFilmGenres(film0);
        List<Integer> genresIDs = databaseFilmGenresStorage.getGenresIdsOfFilm(film0.getId());
        assertEquals(genresIDs.get(0), Integer.valueOf(1));
        assertEquals(genresIDs.get(1), Integer.valueOf(2));
    }

    @Test
    public void testIsFilmHasGenre() {
        Genre genre1 = new Genre(valueOf(1), "Комедия");
        Genre genre2 = new Genre(valueOf(2), "Драма");
        List<Genre> genresList = new ArrayList<>();
        genresList.add(genre1);
        genresList.add(genre2);
        film0.setGenres(genresList);
        databaseFilmGenresStorage.saveFilmGenres(film0);
        assertEquals(databaseFilmGenresStorage.isFilmHasGenre(film0.getId()), true);
    }

    @Test
    public void testAddFriendAndGetFriends() {
        userStorage.create(user0);
        userStorage.create(user1);
        databaseFriendshipStorage.addFriend(user0.getId(), user1.getId());
        Optional<List<User>> friendList = databaseFriendshipStorage.getFriends(user0.getId());
        assertEquals(friendList.get().get(0).getId(), user1.getId());
    }

    @Test
    public void testRemoveFriend() {
        userStorage.create(user0);
        userStorage.create(user1);
        databaseFriendshipStorage.addFriend(user0.getId(), user1.getId());
        databaseFriendshipStorage.removeFriend(user0.getId(), user1.getId());
        Optional<List<User>> friendList = databaseFriendshipStorage.getFriends(user0.getId());
        assertEquals(friendList, Optional.empty());
    }

    @Test
    public void testGetMutualFriends() {
        userStorage.create(user0);
        userStorage.create(user1);
        userStorage.create(user2);
        databaseFriendshipStorage.addFriend(user0.getId(), user1.getId());
        databaseFriendshipStorage.addFriend(user2.getId(), user1.getId());
        Optional<List<User>> mutualFriendList = databaseFriendshipStorage.getMutualFriends(user0.getId(), user2.getId());

        assertEquals(mutualFriendList.get().get(0).getId(), user1.getId());
    }



}