package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class User {
    private int id;

    @JsonIgnore
    private final Map<Integer, FriendshipStatus> friends = new HashMap<>();

    @Email(message = "Email is not valid")
    @NotNull(message = "Электронная почта не может быть пустой!")
    private String email;
    @NotNull(message = "Логин не может быть пустым и содержать пробелы")
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    private String login;
    private String name;
    private LocalDate birthday;
}