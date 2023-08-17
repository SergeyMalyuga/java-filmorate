package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService = new UserService();

    @GetMapping()
    public List<User> getAllUsers() {
        return new ArrayList<>(userService.getAllUsers());
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping()
    public User addChangeUser(@Valid @RequestBody User user) {
        return userService.addChangeUser(user);
    }
}