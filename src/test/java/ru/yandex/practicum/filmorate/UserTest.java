package ru.yandex.practicum.filmorate;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

@SpringBootTest
public class UserTest {
    User user0;
    User user1;
    User user2;

    @BeforeEach
    public void beforeEach() {

        user0 = User.builder()
                .id(Long.valueOf(23))
                .email("email0@mail.ru")
                .login("login0")
                .name("name0")
                .birthday(LocalDate.of(1986,12,28))
                .build();

        user1 = User.builder()
                .id(Long.valueOf(44))
                .email("email1@mail.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(2012,12,28))
                .build();

        user2 = User.builder()
                .id(Long.valueOf(44))
                .email("email1@mail.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(2012,12,28))
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
}
