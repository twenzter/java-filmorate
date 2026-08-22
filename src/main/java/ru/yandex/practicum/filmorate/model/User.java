package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Data
public class User {
    public static final String SPACE_SYMBOL = " ";

    private Long id;
    @NotBlank(message = "User email can't be empty")
    @Email(message = "User email must be on special format")
    private String email;
    @NotBlank(message = "User login can't be empty")
    private String login;
    private String name;
    @NotNull(message = "User birthday can't be empty")
    private LocalDate birthday;
    private Map<Long, FriendshipStatus> friends = new HashMap<>();
}
