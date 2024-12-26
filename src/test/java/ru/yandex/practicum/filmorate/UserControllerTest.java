package ru.yandex.practicum.filmorate;

/*
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
 */

import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class UserControllerTest {
    /*
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserService userService = new UserService(inMemoryUserStorage);
    UserController userController = new UserController(inMemoryUserStorage, userService);

    User user0 = User.builder()
            .id(Long.valueOf(23))
            .email("some@mail.ru")
            .login("somelogin")
            .name("some name")
            .birthday(LocalDate.of(2000, 12, 28))
            .build();

    User user1 = User.builder()
            .id(Long.valueOf(12))
            .email("some1@mail.ru")
            .login("somelogin1")
            .name("some name1")
            .birthday(LocalDate.of(2000, 12, 28))
            .build();

    User user2 = User.builder()
            .id(Long.valueOf(14))
            .email("some2@mail.ru")
            .login("somelogin2")
            .name("somename2")
            .birthday(LocalDate.of(2000, 12, 28))
            .build();

    User userPostman = User.builder()
            .login("dolore")
            .name("Nick Name")
            .email("mail@mail.ru")
            .birthday(LocalDate.of(1946,8,20))
            .build();

    @Test
    public void testFindAllMethodWithEmptyUsersMap() throws Exception {
        try {
            userController.findAll();
        } catch (NotFoundException e) {
            assertEquals(e.getMessage(), "Список юзеров пуст");
        }
    }

    @Test
    public void testFindAllMethodWithFilledUsersMap() throws Exception {
        userController.create(user0);
        assertEquals(userController.findAll().size(), 1);
    }

    @Test
    public void testCreateMethodWithValidObject() throws Exception {
        User testUserObj = userController.create(user0);
        assertEquals(testUserObj, user0);
    }


    @Test
    public void testCreateMethodWithValidObjectForPostman() throws Exception {
        User testUserObj = userController.create(userPostman);
        assertEquals(testUserObj, userPostman);
    }


    @Test
    public void testCreateMethodWithEmptyEmail() throws Exception {
        user0.setEmail(" ");
        try {
            User testUserObj = userController.create(user0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Электронная почта не может быть пустой и должна содержать символ @");
        }
    }

    @Test
    public void testCreateMethodWithEmptyLogin() throws Exception {
        user0.setLogin(" ");
        try {
            User testUserObj = userController.create(user0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Логин не может быть пустым и содержать пробелы");
        }
    }

    @Test
    public void testCreateMethodWithEmptyBirthday() throws Exception {
        user0.setBirthday(null);
        try {
            User testUserObj = userController.create(user0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата рождения должна быть указана");
        }
    }

    @Test
    public void testCreateMethodWithBirthdayInFuture() throws Exception {
        user0.setBirthday(LocalDate.of(2300,12,28));
        try {
            User testUserObj = userController.create(user0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата рождения не может быть в будущем");
        }
    }

    @Test
    public void testUpdateMethodWithNullId() throws Exception {
        user0.setId(null);
        try {
            userController.update(user0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Id должен быть указан");
        }
    }

    @Test
    public void testUpdateMethodWithSameEmail() throws Exception {
        userController.create(user0);
        User user1 = user0;
        user1.setId(Long.valueOf(10));
        userController.create(user1);
        try {
            userController.update(user1);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Этот имейл уже используется");
        }
    }

    @Test
    public void testUpdateMethodWithWrongId() throws Exception {
        userController.create(user0);
        User user1 = (user0);
        user1.setId(Long.valueOf(44));
        try {
            userController.update(user1);
        } catch (NotFoundException e) {
            assertEquals(e.getMessage(), "Юзер с id = 44 не найден");
        }
    }

    @Test
    public void testUpdateMethodWithValidRequest() throws Exception {
        userController.create(user0);
        user0.setEmail("another@Mail");
        user0.setLogin("anotherLogin");
        user0.setName("anotherName");
        user0.setBirthday(LocalDate.of(2013, 12, 28));
        userController.update(user0);
        assertEquals(user0.getEmail(),"another@Mail");
        assertEquals(user0.getLogin(),"anotherLogin");
        assertEquals(user0.getName(), "anotherName");
        assertEquals(user0.getBirthday(),LocalDate.of(2013, 12, 28));
    }

    @Test
    public void addFriendMethodWithValidRequest() throws Exception {
        inMemoryUserStorage.create(user0);
        inMemoryUserStorage.create(user1);
        userController.addFriend(user0.getId(), user1.getId());
        assertEquals(user0.getFriends().contains(user1.getId()),  true);
        assertEquals(user1.getFriends().contains(user0.getId()),  true);
    }

    @Test
    public void removeFriendMethodWithValidRequest() throws Exception {
        inMemoryUserStorage.create(user0);
        inMemoryUserStorage.create(user1);
        userController.addFriend(user0.getId(), user1.getId());
        userController.removeFriend(user0.getId(), user1.getId());
        assertEquals(user0.getFriends().contains(user1.getId()), false);
        assertEquals(user1.getFriends().contains(user0.getId()), false);
    }

    @Test
    void getMutualFriendsMethodTest() {
        inMemoryUserStorage.create(user0);
        inMemoryUserStorage.create(user1);
        inMemoryUserStorage.create(user2);
        userController.addFriend(user1.getId(), user0.getId());
        userController.addFriend(user1.getId(), user2.getId());
        Assertions.assertEquals(userController.getMutualFriends(user0.getId(), user2.getId()).get(0), user1);
    }

    @Test
    void getUserByIdFriendsMethodTest() {
        inMemoryUserStorage.create(user0);
        Assertions.assertEquals(userController.getUserById(user0.getId()), user0);
    }

    @Test
    void getFriendsMethodTest() {
        inMemoryUserStorage.create(user0);
        inMemoryUserStorage.create(user1);
        inMemoryUserStorage.create(user2);
        userController.addFriend(user0.getId(), user1.getId());
        userController.addFriend(user0.getId(), user2.getId());
        List<User> userList = new ArrayList<>(Arrays.asList(user1, user2));
        Assertions.assertEquals(userController.getFriends(user0.getId()), userList);
    }

     */
}