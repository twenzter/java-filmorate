package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends BaseController {
    private static final String EMAIL_SEPARATOR = "@";
    private static final String SPACE_SYMBOL = " ";

    private static final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("GET /users request to get the list of users");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("POST /users request to create a user");

        userChecker(user);

        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("Set user name as login");
            user.setName(user.getLogin());
        }
        log.debug("Set user id");
        user.setId(generateId(users.keySet()));
        log.trace("Add user to list");
        users.put(user.getId(),user);
        log.info("User has been created and added to the list!");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("PUT /users request to update user's data by id");

        if (!users.containsKey(user.getId())) {
            log.warn("Error in user id. User with this id hasn't been found");
            throw new ValidationException("User with this id hasn't been found");
        }
        log.trace("Get oldUser class by id");
        User oldUser = users.get(user.getId());

        userChecker(user);

        log.debug("Set new email for user");
        oldUser.setEmail(user.getEmail());
        log.debug("Set new login for user");
        oldUser.setLogin(user.getLogin());
        log.debug("Set new name for user");
        oldUser.setBirthday(user.getBirthday());

        if (user.getName() != null && !user.getName().isBlank()) {
            log.debug("Set new name for user");
            oldUser.setName(user.getName());
        } else {
            log.trace("Set name as login");
            oldUser.setName(user.getLogin());
        }

        log.info("User data has been updated!");
        return oldUser;
    }

    private void userChecker(User user) {
        if (user.getEmail() == null || !user.getEmail().contains(EMAIL_SEPARATOR)) {
            log.warn("Error in user email. Email can't be empty and must have @");
            throw new ValidationException("Email can't be empty and must have @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(SPACE_SYMBOL)) {
            log.warn("Error in user login. Login can't be empty and contains spaces");
            throw new ValidationException("Login can't be empty and contains spaces");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Error in user birthday. Birthday can't be empty or be in future");
            throw new ValidationException("Birthday can't be empty or be in future");
        }
    }

    public void clear() {
        log.debug("Clearing users map");
        users.clear();
    }

}
