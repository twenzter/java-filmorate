package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Collection<User>> findAll() {
        log.info("GET /users request to get the list of users");
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        log.info("POST /users request to create a user");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("PUT /users request to update user's data by id");
        return ResponseEntity.ok(userService.update(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUser(@PathVariable Long id) {
        log.info("GET /users/{id} request to find user by id");
        return ResponseEntity.ok(userService.findUser(id));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Set<User>> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("PUT /users/{id}/friends/{friendsId} request to add friend");
        return ResponseEntity.ok(userService.addFriend(id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Set<User>> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("DELETE /users/{id}/friends/{friendsId} request to delete friend");
        return ResponseEntity.ok(userService.deleteFriend(id, friendId));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Set<User>> getFriendsList(@PathVariable Long id) {
        log.info("GET /users/{id}/friends request to get friends list");
        return ResponseEntity.ok(userService.getFriendsList(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Set<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("GET /users/{{id}/friends/common/{otherId} request to get common friends");
        return ResponseEntity.ok(userService.getCommonFriends(id, otherId));
    }

}
