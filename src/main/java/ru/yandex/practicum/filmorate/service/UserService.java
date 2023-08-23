package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String addToFriends(int id1, int id2) {
        User user1 = userStorage.getUserById(id1);
        User user2 = userStorage.getUserById(id2);
        if (user1.getFriends().containsKey(user2.getId())) {
            return "Пользователь " + user2.getName() + " уже есть в списке Ваших друзей!";
        }
        user1.getFriends().put(user2.getId(), user2);
        user2.getFriends().put(user1.getId(), user1);
        log.info("Пользователь {} добавлен в друзья", user2.getName());
        return "Пользователь " + user2.getName() + " добавлен в друзья";
    }

    public String deleteFromFriends(int id1, int id2) {
        User user1 = userStorage.getUserById(id1);
        User user2 = userStorage.getUserById(id2);
        if (user1.getFriends().containsKey(user2.getId())) {
            user1.getFriends().remove(user2.getId());
        } else {
            return "Пользователь " + user2.getName() + " не найден в списке Ваших друзей!";
        }
        log.info("Пользователь {} удалён из списка друзей!", user2.getName());
        return "Пользователь " + user2.getName() + " удалён из списка друзей!";
    }

    public List<User> getAllFriends(int id) {
        User user = userStorage.getUserById(id);
        return new ArrayList<>(user.getFriends().values());
    }

    public List<User> getSameFriends(int id1, int id2) {
        Map<Integer, User> userOneListFriends = userStorage.getUserById(id1).getFriends();
        Map<Integer, User> userTwoListFriends = userStorage.getUserById(id2).getFriends();
        List<User> sameFriends = new ArrayList<>();
        for (Integer key : userOneListFriends.keySet()) {
            if (userTwoListFriends.containsKey(key)) {
                sameFriends.add(userOneListFriends.get(key));
            }
        }
        return sameFriends;
    }
}
