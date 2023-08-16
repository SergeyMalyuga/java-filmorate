package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @Email(message = "Email is not valid")
    @NotNull(message = "Электронная почта не может быть пустой!")
    private String email;
    @NotNull(message = "Логин не может быть пустым и содержать пробелы")
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    private String login;
    private String name;
    private LocalDate birthday;
}