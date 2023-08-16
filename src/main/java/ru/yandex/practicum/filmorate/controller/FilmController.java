package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> filmsList = new HashMap<>();
    private static int count = 1;

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        film = validationCheckFilm(film);
        film.setId(count++);
        log.info("Фильм добавлен!");
        filmsList.put(film.getId(), film);
        return film;
    }

    @PutMapping()
    public Film changeFilm(@Valid @RequestBody Film film) {
        film = validationCheckFilm(film);
        if (filmsList.containsKey(film.getId())) {
            filmsList.put(film.getId(),film);
            log.info("Фильм обновлён!");
            return film;
        }
        log.info("Фильма с id: {} не существует!", film.getId());
        throw new ValidationException("Фильма с id: " + film.getId() + " не существует!");
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmsList.values());
    }

    private Film validationCheckFilm(Film film) {
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.info("Маскисальная длина описания - 200 символов");
            throw new ValidationException("Маскисальная длина описания - 200 символов");

        }
        if (film.getDescription() == null) {
            film.setDescription("Описание фильма отстутсвует!");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Дата релиза - не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года");

        }
        if (film.getDuration() == null || film.getDuration().isNegative()) {
            log.info("Продолжительность фильма должна быть положительной!");
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }
        return film;
    }
}