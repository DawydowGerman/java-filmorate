package ru.yandex.practicum.filmorate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

@SpringBootTest
public class UserTest {
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserService userService = new UserService(inMemoryUserStorage);
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage);

    User user0;
    User user1;
    User user2;
    Film film0;
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
        user0 = User.builder()
                .id(Long.valueOf(23))
                .email("email0@mail.ru")
                .login("login0")
                .name("name0")
                .birthday(LocalDate.of(1986,12,28))
                .userService(userService)
                .filmService(filmService)
                .build();

        user1 = User.builder()
                .id(Long.valueOf(44))
                .email("email1@mail.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(2012,12,28))
                .userService(userService)
                .filmService(filmService)
                .build();

        user2 = User.builder()
                .id(Long.valueOf(44))
                .email("email1@mail.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(2012,12,28))
                .userService(userService)
                .filmService(filmService)
                .build();

        film0 = Film.builder()
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
    void getIdMethodTest() {
        Assertions.assertEquals(user0.getId(), Long.valueOf(23));
    }

    @Test
    void setIdMethodTest() {
        user0.setId(Long.valueOf(46));
        Assertions.assertEquals(user0.getId(), Long.valueOf(46));
    }

    @Test
    void getEmailMethodTest() {
        Assertions.assertEquals(user0.getEmail(), "email0@mail.ru");
    }

    @Test
    void setEmailMethodTest() {
        user0.setEmail("another email");
        Assertions.assertEquals(user0.getEmail(),"another email");
    }

    @Test
    void getLoginMethodTest() {
        Assertions.assertEquals(user0.getLogin(), "login0");
    }

    @Test
    void setLoginMethodTest() {
        user0.setLogin("another login");
        Assertions.assertEquals(user0.getLogin(),"another login");
    }

    @Test
    void getNameMethodTest() {
        Assertions.assertEquals(user0.getName(), "name0");
    }

    @Test
    void setNameMethodTest() {
        user0.setName("another name");
        Assertions.assertEquals(user0.getName(),"another name");
    }

    @Test
    void getBirthdayMethodTest() {
        Assertions.assertEquals(user0.getBirthday(), LocalDate.of(1986,12,28));
    }

    @Test
    void setBirthdayMethodTest() {
        user0.setName("another name");
        Assertions.assertEquals(user0.getName(),"another name");
    }

    @Test
    void toStringMethodTest() {
        Assertions.assertEquals(user1.toString(),"User{id=44, email='email1@mail.ru', login='login1', name='name1', birthday=2012-12-28, friends=[]}");
    }

    @Test
    void equalsMethodTest() {
        Assertions.assertEquals(user1.equals(user1), true);
    }

    @Test
    void hashCodeMethodTest() {
        Assertions.assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void addFriendMethodTest() {
        inMemoryUserStorage.create(user0);
        inMemoryUserStorage.create(user1);
        user0.addFriend(user1.getId());
        Assertions.assertEquals(user0.getFriends().size(), 1);
    }

    @Test
    void removeFriendMethodTest() {
        inMemoryUserStorage.create(user0);
        inMemoryUserStorage.create(user1);
        user0.addFriend(user1.getId());
        user0.removeFriend(user1.getId());
        Assertions.assertEquals(user0.getFriends().size(), 0);
    }

    @Test
    void getMutualFriendsMethodTest() {
        inMemoryUserStorage.create(user0);
        inMemoryUserStorage.create(user1);
        inMemoryUserStorage.create(user2);
        user0.addFriend(user1.getId());
        user2.addFriend(user1.getId());
        user0.getMutualFriends(user2.getId());
        Assertions.assertEquals(user0.getMutualFriends(user2.getId()).get(0), user1);
    }

    @Test
    void setToFriendsMethodTest() {
        user0.setToFriends(user1.getId());
        Assertions.assertEquals(user0.getFriends().size(), 1);
    }

    @Test
    void removeFromFriendsMethodTest() {
        user0.setToFriends(user1.getId());
        user0.removeFromFriends(user1.getId());
        Assertions.assertEquals(user0.getFriends().size(), 0);
    }

    @Test
    void isFriendMethodTest() {
        user0.setToFriends(user1.getId());
        Assertions.assertEquals(user0.isFriend(user1.getId()), true);
    }

    @Test
    void giveLikeMethodTest() {
        inMemoryFilmStorage.create(film0);
        user0.giveLike(film0.getId());
        Assertions.assertEquals(film0.getLikes().size(), 1);
    }

    @Test
    void removeLikeMethodTest() {
        inMemoryFilmStorage.create(film0);
        user0.giveLike(film0.getId());
        user0.removeLike(film0.getId());
        Assertions.assertEquals(film0.getLikes().size(), 0);
    }

    @Test
    void getTenMostPopularFilmsMethodTest() {
        film0.setLikes(new HashSet<>(Arrays.asList(
                Long.valueOf(0))));
        inMemoryFilmStorage.create(film0);

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

        Assertions.assertEquals(user0.getMostPopularFilms(10).contains(film0), false);
        Assertions.assertEquals(user0.getMostPopularFilms(10).contains(film1), false);
        Assertions.assertEquals(user0.getMostPopularFilms(10).contains(film2), true);
    }
}
