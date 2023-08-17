package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class UserService {
    private static int count = 1;
    private final Repository repository = new Repository<>();

    public List<User> getAllUsers() {
        return new ArrayList<>(repository.getRepositoryList().values());
    }

    public User addUser(User user) {
        user = validationCheckUser(user);
        user.setId(count++);
        repository.getRepositoryList().put(user.getId(), user);
        log.info("Пользователь добавлен!");
        return user;
    }

    public User addChangeUser(User user) {
        user = validationCheckUser(user);
        if (repository.getRepositoryList().containsKey(user.getId())) {
            repository.getRepositoryList().put(user.getId(), user);
            log.info("Пользователь обнавлён!");
            return user;
        }
        log.info("Пользователя с id: {} не существует!", user.getId());
        throw new ValidationException("Пользователя с id: " + user.getId() + " не существует!");
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
}
