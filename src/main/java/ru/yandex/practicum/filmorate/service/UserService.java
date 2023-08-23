package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

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
        user1.getFriends().put(user2.getId(), user2.getName());
        log.info("Пользователь {} добавлен в друзья", user2.getName());
        return "Пользователь " + user2.getName() + " добавлен в друзья";
    }

    public String deleteFromFriends(int id1, int id2) { //TODO если пользователь не найден
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

    public Map<Integer, String> getAllFriends(int id){
        User user = userStorage.getUserById(id);
        return user.getFriends();
    }

    public Map<Integer, String> getSameFriends(int id1, int id2){
        Map<Integer,String> userOneListFriends = userStorage.getUserById(id1).getFriends();
        Map<Integer,String> userTwoListFriends = userStorage.getUserById(id2).getFriends();
        Map<Integer,String> sameFriends = new HashMap<>();
        for (Integer key : userOneListFriends.keySet()){
           if (userTwoListFriends.containsKey(key)) {
              sameFriends.put(key, userOneListFriends.get(key));
            }
        }
        return sameFriends;
    }
}
