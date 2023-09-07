package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    private RatingType rating;
    private final Set<Integer> userLikes = new HashSet<>();
    private final Set<GenreType> genre = new HashSet<>();
    @NotNull(message = "Название не может быть пустым!")
    @NotBlank(message = "Название не может быть пустым!")
    private String name;
    private String description;
    private LocalDate releaseDate;
    @JsonFormat(pattern = "SECONDS", shape = JsonFormat.Shape.NUMBER)
    private Duration duration;
}