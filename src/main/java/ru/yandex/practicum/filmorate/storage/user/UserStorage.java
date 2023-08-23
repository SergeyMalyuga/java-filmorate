package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);
    User addChangeUser(User user);
    User getUserById(int id);
    List<User> getAllUsers();

}
