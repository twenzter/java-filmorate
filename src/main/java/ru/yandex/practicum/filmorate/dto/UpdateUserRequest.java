package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class UpdateUserRequest {
    @NotNull(message = "id can't be empty")
    private Long id;
    @NotBlank(message = "User email can't be empty")
    @Email(message = "User email must be on special format")
    private String email;
    @NotBlank(message = "User login can't be empty")
    private String login;
    private String name;
    @NotNull(message = "User birthday can't be empty")
    private LocalDate birthday;

    public boolean hasEmail() {
        return ! (email == null || email.isBlank());
    }

    public boolean hasLogin() {
        return ! (login == null || login.isBlank());
    }

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasBirthday() {
        return birthday != null;
    }
}
