package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public String addLike(int filmId, int userId) {
        return filmStorage.addLike(filmId, userId);
    }

    public String deleteLike(int filmId, int userId) {
        return filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count == null) {
            return filmStorage.getAllFilms().stream().sorted((f1, f2)
                            -> f2.getUserLikes().size() - f1.getUserLikes().size())
                    .limit(5)
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

    public List<Mpa> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public Mpa getMpaById(int id) {
        return filmStorage.getMpaById(id);
    }

    public List<Genres> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genres getGenreById(int id) {
        return filmStorage.getGenreById(id);
    }
}
