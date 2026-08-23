package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
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
    public ResponseEntity<Collection<UserDto>> findAll() {
        log.info("GET /users request to get the list of users");
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody NewUserRequest user) {
        log.info("POST /users request to create a user");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@Valid @RequestBody UpdateUserRequest user) {
        log.info("PUT /users request to update user's data by id");
        return ResponseEntity.ok(userService.update(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUser(@PathVariable Long id) {
        log.info("GET /users/{id} request to find user by id");
        return ResponseEntity.ok(userService.findUser(id));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Set<UserDto>> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("PUT /users/{id}/friends/{friendsId} request to add friend");
        return ResponseEntity.ok(userService.addFriend(id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("DELETE /users/{id}/friends/{friendsId} request to delete friend");
        userService.deleteFriend(id, friendId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Set<UserDto>> getFriendsList(@PathVariable Long id) {
        log.info("GET /users/{id}/friends request to get friends list");
        return ResponseEntity.ok(userService.getFriendsList(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Set<UserDto>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("GET /users/{{id}/friends/common/{otherId} request to get common friends");
        return ResponseEntity.ok(userService.getCommonFriends(id, otherId));
    }

}
