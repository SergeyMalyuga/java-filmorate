package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return new ArrayList<>(userService.getUserStorage().getAllUsers());
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        return userService.getUserStorage().addUser(user);
    }

    @PutMapping()
    public User addChangeUser(@Valid @RequestBody User user) {
        return userService.getUserStorage().addChangeUser(user);
    }
}