package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataByIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id){
      return  userService.getUserStorage().getUserById(id);
    }

    @GetMapping("{id}/friends")
    public Map<Integer,String> getAllFriends(@PathVariable int id){
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Map<Integer,String> getSameFriends(@PathVariable int id, @PathVariable int otherId){
        return userService.getSameFriends(id, otherId);
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        return userService.getUserStorage().addUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String addToFriends(@PathVariable int id, @PathVariable int friendId){
       return userService.addToFriends(id, friendId);
    }

    @PutMapping()
    public User addChangeUser(@Valid @RequestBody User user) {
        return userService.getUserStorage().addChangeUser(user);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFromFriends(@PathVariable int id, @PathVariable int friendId){
        return userService.deleteFromFriends(id,friendId);
    }
}