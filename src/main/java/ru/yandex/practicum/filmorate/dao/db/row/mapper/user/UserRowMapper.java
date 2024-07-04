package ru.yandex.practicum.filmorate.dao.db.row.mapper.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MapperException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper {
    @Override
    public User mapRow(ResultSet rs, int rowNum) {
        try {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("EMAIL"));
            user.setLogin(rs.getString("LOGIN"));
            user.setName(rs.getString("NAME"));
            user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
            return user;
        } catch (SQLException e) {
            throw new MapperException("Could not map user row " + rowNum, e);
        }
    }
}
