package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;

@Service
@Data
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public void addToFriends(User user1, User user2){
        user1.getFriends().add(user2.getId());
    }

    public void deleteFromFriends(User user1, User user2){ //TODO если пользователь не найден
        if (user1.getFriends().contains(user2.getId())) {
            user1.getFriends().remove(user2.getId());
        }
    }

    public Set<Integer> getAllFriends(User user){
        return user.getFriends();
    }
}
