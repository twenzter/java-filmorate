package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class UserServiceTest {
    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();
    private final UserService userService = new UserService(userStorage);
    private NewUserRequest user;

    @BeforeEach
    public void beforeEach() {
        user = new NewUserRequest();
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
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("email@mail.ru");
        userDto.setLogin("login");
        userDto.setName("login");
        userDto.setBirthday(LocalDate.of(1999,02,01));
        Assertions.assertEquals(userDto,userService.create(user));
    }

    @Test
    public void getUsersList() {
        NewUserRequest user1 = new NewUserRequest();
        user1.setEmail("login1@mail.ru");
        user1.setLogin("login1");
        user1.setBirthday(LocalDate.of(1999,2,3));
        userService.create(user1);

        NewUserRequest user2 = new NewUserRequest();
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

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("email@mail.ru");
        userDto.setLogin("login");
        userDto.setName("login");
        userDto.setBirthday(LocalDate.now());
        Assertions.assertEquals(userDto,userService.create(user));
    }

    @Test
    public void postUserWithBirthdayInPast() {
        user.setBirthday(LocalDate.now().minusDays(100));

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("email@mail.ru");
        userDto.setLogin("login");
        userDto.setName("login");
        userDto.setBirthday(LocalDate.now().minusDays(100));
        Assertions.assertEquals(userDto,userService.create(user));
    }

    @Test
    public void postUserNullNameWillBeAsLogin() {
        userService.create(user);
        Assertions.assertEquals(userStorage.findOne(1L).get().getName(),
                userStorage.findOne(1L).get().getLogin());
    }

    @Test
    public void postUserEmptyNameWillBeAsLogin() {
        user.setName("   ");
        userService.create(user);
        Assertions.assertEquals(userStorage.findOne(1L).get().getName(),
                userStorage.findOne(1L).get().getLogin());
    }

    @Test
    public void postUserCheckNameWillBeUnique() {
        user.setName("name");
        userService.create(user);
        Assertions.assertNotEquals(userStorage.findOne(1L).get().getName(),
                userStorage.findOne(1L).get().getLogin());
    }

    @Test
    public void updateUser() {
        userService.create(user);

        UpdateUserRequest user2 = new UpdateUserRequest();
        user2.setId(1L);
        user2.setEmail("email2@mail.ru");
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setBirthday(LocalDate.of(2000,2,1));

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("email2@mail.ru");
        userDto.setLogin("login2");
        userDto.setName("name2");
        userDto.setBirthday(LocalDate.of(2000,2,1));
        Assertions.assertEquals(userDto,userService.update(user2));
    }

    @Test
    public void updateUserWithInvalidId() {
        userService.create(user);

        UpdateUserRequest user2 = new UpdateUserRequest();
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
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("email@mail.ru");
        userDto.setLogin("login");
        userDto.setName("login");
        userDto.setBirthday(LocalDate.of(1999,02,01));

        Assertions.assertEquals(userDto, userService.findUser(1L));
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
    public void addFriend() {
        userService.create(user);
        userService.create(user);

        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setEmail("email@mail.ru");
        userDto.setLogin("login");
        userDto.setName("login");
        userDto.setBirthday(LocalDate.of(1999,02,01));

        Assertions.assertEquals(Set.of(userDto), userService.addFriend(1L,2L));
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
            Assertions.assertEquals("User with friendId is already add to friends",e.getDescription());
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
        NewUserRequest user1 = new NewUserRequest();
        user1.setEmail("email@mail.ru");
        user1.setLogin("login");
        user1.setBirthday(LocalDate.of(1999,2,1));
        NewUserRequest user2 = new NewUserRequest();
        user2.setEmail("email@mail.ru");
        user2.setLogin("login");
        user2.setBirthday(LocalDate.of(1999,2,1));
        NewUserRequest user3 = new NewUserRequest();
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
        set.add(userStorage.findOne(3L).get());

        Assertions.assertEquals(userService.getCommonFriends(2L,4L),
                set.stream().map(UserMapper::mapToUserDto).collect(Collectors.toSet()));
    }
}
