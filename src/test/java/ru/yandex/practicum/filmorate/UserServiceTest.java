package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class UserServiceTest {
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final UserService userService = new UserService(userStorage);
    private User user;

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setEmail("email@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1999,2,1));
        userStorage.clear();
    }

    @Test
    public void getUserEmptyList() {
        System.out.println(userService.findAll());
        Assertions.assertTrue(userService.findAll().isEmpty());
    }

    @Test
    public void postUser() {
        Assertions.assertEquals(user,userService.create(user));
    }

    @Test
    public void getUsersList() {
        User user1 = new User();
        user1.setEmail("login1@mail.ru");
        user1.setLogin("login1");
        user1.setBirthday(LocalDate.of(1999,2,3));
        userService.create(user1);

        User user2 = new User();
        user2.setEmail("login2@mail.ru");
        user2.setLogin("login2");
        user2.setBirthday(LocalDate.of(1979,7,3));
        userService.create(user2);
        Assertions.assertEquals(2, userService.findAll().size());
    }


    @Test
    public void postUSerWithSpacesInLogin() {
        user.setLogin("l  og in");
        try {
            userService.create(user);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Login can't contain spaces", e.getDescription());
        }
    }

    @Test
    public void postUserWithBirthdayInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        try {
            userService.create(user);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Birthday can't be in future", e.getDescription());
        }
    }

    @Test
    public void postUserWithBirthdayInRightNow() {
        user.setBirthday(LocalDate.now());
        Assertions.assertEquals(user,userService.create(user));
    }

    @Test
    public void postUserWithBirthdayInPast() {
        user.setBirthday(LocalDate.now().minusDays(100));
        Assertions.assertEquals(user,userService.create(user));
    }

    @Test
    public void postUserNullNameWillBeAsLogin() {
        userService.create(user);
        Assertions.assertEquals(user.getName(),user.getLogin());
    }

    @Test
    public void postUserEmptyNameWillBeAsLogin() {
        user.setName("   ");
        userService.create(user);
        Assertions.assertEquals(user.getName(),user.getLogin());
    }

    @Test
    public void postUserCheckNameWillBeUnique() {
        user.setName("name");
        userService.create(user);
        Assertions.assertNotEquals(user.getName(),user.getLogin());
    }

    @Test
    public void updateUser() {
        userService.create(user);

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("email2@mail.ru");
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setBirthday(LocalDate.of(2000,2,1));
        Assertions.assertEquals(user2,userService.update(user2));
    }

    @Test
    public void updateUserWithInvalidId() {
        userService.create(user);

        User user2 = new User();
        user2.setId(2L);
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setBirthday(LocalDate.of(2000,2,1));

        try {
            userService.update(user2);
            Assertions.fail();
        } catch (NotFoundException e) {
            Assertions.assertEquals("User with id 2 hasn't been found", e.getDescription());
        }
    }

    @Test
    public void findUserById() {
        userService.create(user);
        Assertions.assertEquals(userService.findUser(1L), user);
    }

    @Test
    public void findUserByNonExistentId() {
        try {
            userService.findUser(1L);
            Assertions.fail();
        } catch (NotFoundException e) {
            Assertions.assertEquals("User with id 1 hasn't been found", e.getDescription());
        }

    }

    @Test
    public void addLikeToFilmByIdAndFriendId() {
        userService.create(user);
        userService.create(user);

        Set<User> set = new HashSet<>();
        set.add(userStorage.get(1L));
        set.add(userStorage.get(2L));

        Assertions.assertEquals(userService.addFriend(1L,2L), set);
    }

    @Test
    public void addFriendToUserAgainByIdAndSameFriendId() {
        userService.create(user);
        userService.create(user);
        userService.addFriend(1L,2L);
        try {
            userService.addFriend(1L,2L);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("User with friendId is already in friends",e.getDescription());
        }

    }

    @Test
    public void deleteFriendToUserByIdAndFriendId() {
        userService.create(user);
        userService.create(user);
        userService.addFriend(1L,2L);
        userService.deleteFriend(1L,2L);
    }

    @Test
    public void commonFriendCheck() {
        User user1 = new User();
        user1.setEmail("email@mail.ru");
        user1.setLogin("login");
        user1.setBirthday(LocalDate.of(1999,2,1));
        User user2 = new User();
        user2.setEmail("email@mail.ru");
        user2.setLogin("login");
        user2.setBirthday(LocalDate.of(1999,2,1));
        User user3 = new User();
        user3.setEmail("email@mail.ru");
        user3.setLogin("login");
        user3.setBirthday(LocalDate.of(1999,2,1));

        userService.create(user);
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);

        userService.addFriend(1L,2L);
        userService.addFriend(1L,4L);
        userService.addFriend(2L,4L);
        userService.addFriend(2L,3L);
        userService.addFriend(4L,3L);

        Set<User> set = new HashSet<>();
        set.add(userStorage.get(1L));
        set.add(userStorage.get(3L));

        Assertions.assertEquals(userService.getCommonFriends(2L,4L),set);
    }
}
