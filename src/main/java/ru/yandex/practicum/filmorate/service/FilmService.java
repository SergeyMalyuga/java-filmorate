package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataByIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public String addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.getUserLikes().add(userId);
        return "Вы поставили like фильму: " + film.getName();
    }

    public String deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film.getUserLikes().contains(userId)) {
            film.getUserLikes().remove(userId);
            return "Лайк с фильма: " + film.getName() + " удалён: ";
        }
        throw new DataByIdException("Вы не лайкали: " + film.getName());

    }

    public List<Film> getPopularFilms(Integer count) {
        if (count == null) {
            return filmStorage.getAllFilms().stream().sorted((f1, f2)
                            -> f2.getUserLikes().size() - f1.getUserLikes().size())
                    .limit(10)
                    .collect(Collectors.toList());
        }
        return filmStorage.getAllFilms().stream().sorted((f1, f2)
                        -> f2.getUserLikes().size() - f1.getUserLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film changeFilm(Film film) {
        return filmStorage.changeFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }
}
