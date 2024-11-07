package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ru.yandex.practicum.filmorate.model.User;

@SpringBootTest
public class UserControllerTest {
    UserController userController = new UserController();

    User user0 = User.builder()
            .id(Long.valueOf(23))
            .email("some@mail.ru")
            .login("somelogin")
            .name("some name")
            .birthday(Instant.parse("2000-12-28T00:00:00.00Z"))
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
        user0.setBirthday(Instant.parse("2300-12-28T00:00:00.00Z"));
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
        user0.setBirthday(Instant.parse("2013-12-28T00:00:00.00Z"));

        userController.update(user0);

        assertEquals(user0.getEmail(),"another@Mail");
        assertEquals(user0.getLogin(),"anotherLogin");
        assertEquals(user0.getName(), "anotherName");
        assertEquals(user0.getBirthday(),Instant.parse("2013-12-28T00:00:00.00Z"));
    }
}
