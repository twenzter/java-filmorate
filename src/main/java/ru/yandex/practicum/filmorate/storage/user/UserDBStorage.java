package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.mappers.UserRowMapper;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class UserDBStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("login", user.getLogin());
        parameters.put("name", user.getName());
        parameters.put("birthday", user.getBirthday());

        Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
        user.setId(id.longValue());

        return user;
    }

    @Override
    public User update(User user) {
        String sql =
                "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<User> findOne(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id);
        if (users.isEmpty()) {
            return Optional.empty();
        }
        User user = users.getFirst();
        user.setFriends(findFriends(user.getId()));
        return Optional.of(user);
    }

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
        for (User user: users) {
            user.setFriends(findFriends(user.getId()));
        }
        return users;
    }

    @Override
    public Map<Long, FriendshipStatus> addFriend(Long id, Long friendId) {
        String sqlCheckStatus = "SELECT friendship_status_id FROM users_friends WHERE user_id = ? AND friend_id = ?";
        List<Long> friendStatus = jdbcTemplate.query(sqlCheckStatus,
                (rs,rowNum) -> rs.getLong("friendship_status_id"), friendId, id);

        Long status = FriendshipStatus.UNCONFIRMED.getId();
        if (!friendStatus.isEmpty()) {
            status = FriendshipStatus.CONFIRMED.getId();
            String sqlConfirmedFriend =
                    "UPDATE users_friends SET friendship_status_id = ? WHERE user_id = ? AND friend_id = ?";
            jdbcTemplate.update(sqlConfirmedFriend, status, friendId, id);
            jdbcTemplate.update(sqlConfirmedFriend, status, id, friendId);
        }

        String sqlUnconfirmedFriend =
                "MERGE INTO users_friends (user_id, friend_id, friendship_status_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlUnconfirmedFriend, id, friendId, status);
        return findFriends(id);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        String sqlDeleteFriend = "DELETE FROM users_friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlDeleteFriend, id, friendId);

        Long status = FriendshipStatus.UNCONFIRMED.getId();
        String sqlUpdateFriend = "UPDATE users_friends SET friendship_status_id = ? WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlUpdateFriend, status, friendId, id);
    }

    @Override
    public Map<Long, FriendshipStatus> findFriends(Long id) {
        String sql = "SELECT friend_id, friendship_status_id FROM users_friends WHERE user_id = ?";
        Map<Long, FriendshipStatus> friendMap = new HashMap<>();

        jdbcTemplate.query(sql, (rs) -> {
            Long friendId = rs.getLong("friend_id");
            FriendshipStatus friendshipStatus = FriendshipStatus
                    .fromId(rs.getLong("friendship_status_id")).orElse(null);

            friendMap.put(friendId, friendshipStatus);
        }, id);
        return friendMap;
    }

}
