package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataByIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
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

    @Override
    public String addToFriends(int id1, int id2) {
        User user1 = getUserById(id1);
        User user2 = getUserById(id2);
        HashMap<User, FriendshipStatus> userFriendshipStatus = new HashMap<>();
        userFriendshipStatus.put(user2, FriendshipStatus.CONFIRMED);
        if (user1.getFriends().containsKey(user2.getId())) {
            return "Пользователь " + user2.getName() + " уже есть в списке Ваших друзей!";
        }
        user1.getFriends().put(user2.getId(), FriendshipStatus.CONFIRMED);
        userFriendshipStatus.clear();
        userFriendshipStatus.put(user1, FriendshipStatus.CONFIRMED);
        user2.getFriends().put(user1.getId(), FriendshipStatus.CONFIRMED);
        log.info("Пользователь {} добавлен в друзья", user2.getName());
        return "Пользователь " + user2.getName() + " добавлен в друзья";
    }

    @Override
    public String deleteFromFriends(int id1, int id2) { // TODO
        User user1 = getUserById(id1);
        User user2 = getUserById(id2);
        if (user1.getFriends().containsKey(user2.getId())) {
            user1.getFriends().remove(user2.getId());
        } else {
            return "Пользователь " + user2.getName() + " не найден в списке Ваших друзей!";
        }
        log.info("Пользователь {} удалён из списка друзей!", user2.getName());
        return "Пользователь " + user2.getName() + " удалён из списка друзей!";
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
        return Optional.ofNullable(repository.get(id))
                .orElseThrow(() -> new DataByIdException("Пользователь с id: " + id + " не найден."));
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
