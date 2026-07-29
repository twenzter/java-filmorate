package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.info("GET /users request to get the list of users");
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("POST /users request to create a user");
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("PUT /users request to update user's data by id");
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable Long id) {
        log.info("GET /users/{id} request to find user by id");
        return userService.findUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Set<User> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("PUT /users/{id}/friends/{friendsId} request to add friend");
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Set<User> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("DELETE /users/{id}/friends/{friendsId} request to delete friend");
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriendsList(@PathVariable Long id) {
        log.info("GET /users/{id}/friends request to get friends list");
        return userService.getFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("GET /users/{{id}/friends/common/{otherId} request to get common friends");
        return userService.getCommonFriends(id, otherId);
    }

}
