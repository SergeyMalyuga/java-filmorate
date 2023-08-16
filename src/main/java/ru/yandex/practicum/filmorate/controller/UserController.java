package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> usersList = new HashMap<>();
    private static int count = 1;

    @GetMapping()
    public List<User> getAllUsers() {
        return new ArrayList<>(usersList.values());
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        user = validationCheckUser(user);
        user.setId(count++);
        usersList.put(user.getId(), user);
        log.info("Пользователь добавлен!");
        return user;
    }

    @PutMapping()
    public User addChangeUser(@Valid @RequestBody User user) {
        user = validationCheckUser(user);
        if (usersList.containsKey(user.getId())) {
            usersList.put(user.getId(), user);
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