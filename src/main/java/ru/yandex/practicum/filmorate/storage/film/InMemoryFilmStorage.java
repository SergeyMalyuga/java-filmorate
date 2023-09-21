package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataByIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;

@Data
@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static int count = 1;
    private final Map<Integer, Film> repository = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        film = validationCheckFilm(film);
        film.setId(count++);
        log.info("Фильм добавлен!");
        repository.put(film.getId(), film);
        return film;
    }

    @Override
    public Film changeFilm(Film film) {
        film = validationCheckFilm(film);
        if (repository.containsKey(film.getId())) {
            repository.put(film.getId(), film);
            log.info("Фильм обновлён!");
            return film;
        }
        log.info("Фильма с id: {} не существует!", film.getId());
        throw new DataByIdException("Фильма с id: " + film.getId() + " не существует!");
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public String addLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        film.getUserLikes().add(userId);
        return "Вы поставили like фильму: " + film.getName();
    }

    @Override
    public String deleteLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        if (film.getUserLikes().contains(userId)) {
            film.getUserLikes().remove(userId);
            return "Лайк с фильма: " + film.getName() + " удалён: ";
        }
        throw new DataByIdException("Вы не лайкали: " + film.getName());

    }

    @Override
    public List<Mpa> getAllMpa() {
        return new ArrayList<>();
    }

    @Override
    public Mpa getMpaById(int id) {
        return null;
    }

    @Override
    public List<Genres> getAllGenres() {
        return new ArrayList<>();
    }

    @Override
    public Genres getGenreById(int id) {
        return null;
    }

    @Override
    public Film getFilmById(int id) {
        return Optional.ofNullable(repository.get(id))
                .orElseThrow(() -> new DataByIdException("Фильм с id: " + id + " не найден."));
    }

    private Film validationCheckFilm(Film film) {
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.info("Маскисальная длина описания - 200 символов");
            throw new ValidationException("Маскисальная длина описания - 200 символов");

        }
        if (film.getDescription() == null) {
            film.setDescription("Описание фильма отстутсвует!");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate()
                .isBefore(LocalDate.of(1895, 12, 28))) {
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

