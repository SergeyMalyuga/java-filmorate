package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class FilmService {
    private static int count = 1;
    private final Repository repository = new Repository();

    public Film addFilm(Film film) {
        film = validationCheckFilm(film);
        film.setId(count++);
        log.info("Фильм добавлен!");
        repository.getRepositoryList().put(film.getId(), film);
        return film;
    }

    public Film changeFilm(Film film) {
        film = validationCheckFilm(film);
        if (repository.getRepositoryList().containsKey(film.getId())) {
            repository.getRepositoryList().put(film.getId(), film);
            log.info("Фильм обновлён!");
            return film;
        }
        log.info("Фильма с id: {} не существует!", film.getId());
        throw new ValidationException("Фильма с id: " + film.getId() + " не существует!");
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(repository.getRepositoryList().values());
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
