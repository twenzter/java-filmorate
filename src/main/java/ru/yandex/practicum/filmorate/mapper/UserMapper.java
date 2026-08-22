package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User mapToUser(NewUserRequest newUser) {
        User user = new User();
        user.setEmail(newUser.getEmail());
        user.setLogin(newUser.getLogin());
        user.setBirthday(newUser.getBirthday());
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            user.setName(newUser.getLogin());
        } else {
            user.setName(newUser.getName());
        }
        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setBirthday(user.getBirthday());
        userDto.setName(user.getName());
        return userDto;
    }

    public static User updateUserFields(User user, UpdateUserRequest updatedUser) {
        if (updatedUser.hasEmail()) {
            user.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.hasLogin()) {
            user.setLogin(updatedUser.getLogin());
        }
        if (updatedUser.hasBirthday()) {
            user.setBirthday(updatedUser.getBirthday());
        }
        if (updatedUser.hasName()) {
            user.setName(updatedUser.getName());
        }
        return user;
    }
}
