package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum FriendshipStatus {
    CONFIRMED(1L, "CONFIRMED"),
    UNCONFIRMED(2L, "UNCONFIRMED");

    private final Long id;
    private final String name;

    public static Optional<FriendshipStatus> fromId(Long id) {
        for (FriendshipStatus friendshipStatus: values()) {
            if (friendshipStatus.getId().equals(id)) {
                return Optional.of(friendshipStatus);
            }
        }
        return Optional.empty();
    }
}
