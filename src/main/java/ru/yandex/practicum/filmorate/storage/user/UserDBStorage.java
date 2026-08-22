package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.mappers.UserRowMapper;

import java.util.*;

@Repository
@RequiredArgsConstructor
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
        user.setFriends(getUserFriendsById(user.getId()));
        return Optional.of(user);
    }

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
        for (User user: users) {
            user.setFriends(getUserFriendsById(user.getId()));
        }
        return users;
    }

    private Map<Long, FriendshipStatus> getUserFriendsById(Long id) {
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
