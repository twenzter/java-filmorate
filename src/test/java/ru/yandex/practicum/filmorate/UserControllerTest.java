package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTest {
    private static final UserController userController = new UserController();
    private static User user;

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setEmail("email@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1999,2,1));
        userController.clear();
    }

    @Test
    public void getUserEmptyList() {
        System.out.println(userController.findAll());
        Assertions.assertTrue(userController.findAll().isEmpty());
    }

    @Test
    public void postUser() {
        Assertions.assertEquals(user,userController.create(user));
    }

    @Test
    public void getUsersList() {
        User user1 = new User();
        user1.setEmail("login1@mail.ru");
        user1.setLogin("login1");
        user1.setBirthday(LocalDate.of(1999,2,3));
        userController.create(user1);

        User user2 = new User();
        user2.setEmail("login2@mail.ru");
        user2.setLogin("login2");
        user2.setBirthday(LocalDate.of(1979,7,3));
        userController.create(user2);
        Assertions.assertEquals(2, userController.findAll().size());
    }

    @Test
    public void postUserWithoutEmail() {
        User user1 = new User();
        user1.setLogin("login");
        user1.setBirthday(LocalDate.of(1979,7,3));
        try {
            userController.create(user1);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Email can't be empty and must have @", e.getMessage());
        }
    }

    @Test
    public void postUserWithoutSeparatorInEmail() {
        user.setEmail("email");
        try {
            userController.create(user);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Email can't be empty and must have @", e.getMessage());
        }
    }

    @Test
    public void postUserWithoutLogin() {
        User user1 = new User();
        user1.setEmail("email@mail.ru");
        user1.setBirthday(LocalDate.of(1979,7,3));
        try {
            userController.create(user1);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Login can't be empty and contains spaces", e.getMessage());
        }
    }

    @Test
    public void postUserWithEmptyLogin() {
        user.setLogin("");
        try {
            userController.create(user);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Login can't be empty and contains spaces", e.getMessage());
        }
    }

    @Test
    public void postUSerWithSpacesInLogin() {
        user.setLogin("l  og in");
        try {
            userController.create(user);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Login can't be empty and contains spaces", e.getMessage());
        }
    }

    @Test
    public void postUserWithoutBirthday() {
        User user1 = new User();
        user1.setLogin("login");
        user1.setEmail("email@mail.ru");
        try {
            userController.create(user1);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Birthday can't be empty or be in future", e.getMessage());
        }
    }

    @Test
    public void postUserWithBirthdayInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        try {
            userController.create(user);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Birthday can't be empty or be in future", e.getMessage());
        }
    }

    @Test
    public void postUserWithBirthdayInRightNow() {
        user.setBirthday(LocalDate.now());
        Assertions.assertEquals(user,userController.create(user));
    }

    @Test
    public void postUserWithBirthdayInPast() {
        user.setBirthday(LocalDate.now().minusDays(100));
        Assertions.assertEquals(user,userController.create(user));
    }

    @Test
    public void postUserNullNameWillBeAsLogin() {
        userController.create(user);
        Assertions.assertEquals(user.getName(),user.getLogin());
    }

    @Test
    public void postUserEmptyNameWillBeAsLogin() {
        user.setName("   ");
        userController.create(user);
        Assertions.assertEquals(user.getName(),user.getLogin());
    }

    @Test
    public void postUserCheckNameWillBeUnique() {
        user.setName("name");
        userController.create(user);
        Assertions.assertNotEquals(user.getName(),user.getLogin());
    }

    @Test
    public void updateUser() {
        userController.create(user);

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("email2@mail.ru");
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setBirthday(LocalDate.of(2000,2,1));
        Assertions.assertEquals(user2,userController.update(user2));
    }

    @Test
    public void updateUserWithInvalidId() {
        userController.create(user);

        User user2 = new User();
        user2.setId(2L);
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setBirthday(LocalDate.of(2000,2,1));

        try {
            userController.update(user2);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("User with this id hasn't been found", e.getMessage());
        }
    }
}
