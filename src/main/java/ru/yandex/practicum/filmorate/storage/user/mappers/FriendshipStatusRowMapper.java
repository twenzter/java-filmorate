package ru.yandex.practicum.filmorate.storage.user.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipStatusRowMapper implements RowMapper<FriendshipStatus> {

    public FriendshipStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FriendshipStatus.fromId(rs.getLong("friendship_status_id")).orElse(null);
    }
}
