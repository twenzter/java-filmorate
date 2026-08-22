package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NewUserRequest {
    @NotBlank(message = "User email can't be empty")
    @Email(message = "User email must be on special format")
    private String email;
    @NotBlank(message = "User login can't be empty")
    private String login;
    private String name;
    @NotNull(message = "User birthday can't be empty")
    private LocalDate birthday;
}
