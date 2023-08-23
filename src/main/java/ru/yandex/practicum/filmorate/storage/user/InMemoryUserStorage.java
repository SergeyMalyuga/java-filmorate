package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataByIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Data
@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static int count = 1;
    private final Map<Integer, User> repository = new HashMap<>();

    public List<User> getAllUsers() {
        return new ArrayList<>(repository.values());
    }

    public User addUser(User user) {
        user = validationCheckUser(user);
        user.setId(count++);
        repository.put(user.getId(), user);
        log.info("Пользователь добавлен!");
        return user;
    }

    @Override
    public User getUserById(int id) {
        Optional<User> user = getAllUsers().stream().filter(f -> f.getId() == id).findFirst();
        if (!user.isPresent()) {
            log.info("Пользователь с id: " + id + " не найден.");
            throw new DataByIdException("Пользователь с id:" + id + " не найден.");
        }
        return user.get();
    }

    public User addChangeUser(User user) {
        user = validationCheckUser(user);
        if (repository.containsKey(user.getId())) {
            repository.put(user.getId(), user);
            log.info("Пользователь обнавлён!");
            return user;
        }
        log.info("Пользователя с id: {} не существует!", user.getId());
        throw new DataByIdException("Пользователя с id: " + user.getId() + " не существует!");
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