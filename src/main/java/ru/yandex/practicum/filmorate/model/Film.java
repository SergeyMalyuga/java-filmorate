package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    @NotNull(message = "Название не может быть пустым!")
    @NotBlank(message = "Название не может быть пустым!")
    private String name;
    private String description;
    private LocalDate releaseDate;
    @JsonFormat(pattern = "SECONDS", shape = JsonFormat.Shape.NUMBER)
    private Duration duration;
}
