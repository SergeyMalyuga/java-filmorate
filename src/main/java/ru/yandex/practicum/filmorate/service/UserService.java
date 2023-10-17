package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Data
@Slf4j
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String addToFriends(int id1, int id2) {
        return userStorage.addToFriends(id1, id2);
    }

    public String deleteFromFriends(int id1, int id2) {
        return userStorage.deleteFromFriends(id1, id2);
    }

    public List<User> getAllFriends(int id) {
        User user = userStorage.getUserById(id);
        List<User> allFriendsList = new ArrayList<>();
        for (Integer userId : user.getFriends().keySet()) {
            allFriendsList.add(getUserById(userId));
        }
        return allFriendsList;
    }

    public List<User> getSameFriends(int id1, int id2) {
        Map<Integer, FriendshipStatus> userOneListFriends = userStorage.getUserById(id1).getFriends();
        Map<Integer, FriendshipStatus> userTwoListFriends = userStorage.getUserById(id2).getFriends();
        List<User> sameFriends = new ArrayList<>();
        for (Integer key : userOneListFriends.keySet()) {
            if (userTwoListFriends.containsKey(key)) {
                sameFriends.add(userStorage.getUserById(key));
            }
        }
        return sameFriends;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User addChangeUser(User user) {
        return userStorage.addChangeUser(user);
    }
}
