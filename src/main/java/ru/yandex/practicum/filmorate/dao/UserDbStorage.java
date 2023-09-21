package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataByIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {

    JdbcTemplate jdbcTemplate;
    private static int count = 0;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        validationCheckUser(user);
        user.setId(setCountId());
        String sql = "INSERT INTO users(user_id, user_name, login, email, birthday) " +
                "VALUES(?,?,?,?,?);";
        jdbcTemplate.update(sql, user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        log.info("Новый пользователь добавлен!");
        return user;
    }

    @Override
    public User addChangeUser(User user) {
        validationCheckUser(user);
        getUserById(user.getId());
        String sql = "Update users SET user_name = ?, login = ?, email = ?, birthday = ? " +
                "WHERE user_id = ?;";
        jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(),
                user.getId());
        log.info("Данные пользователя изменены!");
        return user;
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        User user;
        if (sqlRowSet.next()) {
            user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs), id);
        } else {
            throw new DataByIdException("Пользователь с id: " + id + " не найден.");
        }
        return addFriendsToUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users ";
        log.info("Список пользователей получен.");
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    private User validationCheckUser(User user) {
        if (user.getLogin() != null && (user.getName() == null || user.getName().isBlank())) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения не может быть в будущем!");
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
        return user;
    }

    @Override
    public String addToFriends(int id1, int id2) {
        User user2 = getUserById(id2);
        String sql = "SELECT * FROM USERS u INNER JOIN FRIENDS f ON u.USER_ID = f.USER_ID WHERE u.USER_ID = ? " +
                "AND user_friend_id = ?;";
        String sqlUpdate = "INSERT INTO friends(friends_id, user_id, user_friend_id, friendship_id) VALUES(?,?,?,?)";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id1, id2);
        if (sqlRowSet.first()) {
            return "Пользователь " + user2.getName() + " уже есть в списке Ваших друзей!";
        }
        FriendshipStatus status = getFriendshipStatus(id2, id1);
        jdbcTemplate.update(sqlUpdate, count++, id1, id2, status.ordinal());
        log.info("Пользователь {} добавлен в друзья", user2.getName());
        return "Пользователь " + user2.getName() + " добавлен в друзья";
    }

    @Override
    public String deleteFromFriends(int id1, int id2) {
        User user1 = getUserById(id1);
        User user2 = getUserById(id2);
        String sql = "SELECT * FROM friends WHERE user_id = ? AND user_friend_id = ?;";
        SqlRowSet userList = jdbcTemplate.queryForRowSet(sql, id1, id2);
        String sqlDELETE = "DELETE FROM friends WHERE user_id = ? AND user_friend_id = ?;";
        String sqlUpdateFriends = "UPDATE friends SET friendship_id = ? WHERE user_id = ? AND user_friend_id = ?;";
        if (userList.first()) {
            user1.getFriends().remove(user2.getId());
            jdbcTemplate.update(sqlDELETE, id1, id2);
            jdbcTemplate.update(sqlUpdateFriends, 1, id2, id1);
            log.info("Пользователь {} удалён из списка друзей!", user2.getName());
            return "Пользователь " + user2.getName() + " удалён из списка друзей!";
        }
        return "Пользователь " + user2.getName() + " не найден в списке Ваших друзей!";
    }

    private User addFriendsToUser(User user) {
        String sql = "SELECT * FROM friends WHERE user_id = ?;";
        String sqlMakeUserFriend = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, user.getId());
        while (sqlRowSet.next()) {
            User userFriend = jdbcTemplate.query(sqlMakeUserFriend, (rs, rowNum) -> makeUser(rs),
                    sqlRowSet.getInt("user_friend_id")).get(0);
            user.getFriends().put(userFriend.getId(),
                    FriendshipStatus.values()[sqlRowSet.getInt("friendship_id")]);
        }
        return user;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String email = rs.getString("email");
        String userName = rs.getString("user_name");
        String login = rs.getString("login");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return User.builder().id(id).name(userName)
                .login(login)
                .email(email)
                .birthday(birthday).build();
    }

    private FriendshipStatus getFriendshipStatus(int id1, int id2) {
        String sql = "SELECT * FROM USERS u INNER JOIN FRIENDS f ON u.USER_ID = f.USER_ID WHERE u.USER_ID = ? " +
                "AND user_friend_id = ?;";
        String sqlUpdate = "UPDATE friends SET friendship_id = ? WHERE user_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id1, id2);
        if (sqlRowSet.first()) {
            jdbcTemplate.update(sqlUpdate, 0, id1);
            return FriendshipStatus.CONFIRMED;
        }
        return FriendshipStatus.UNCONFIRMED;
    }

    private int setCountId() {
        if (getAllUsers().size() == 0) {
            return 1;
        }
        return getAllUsers().size() + 1;
    }
}
