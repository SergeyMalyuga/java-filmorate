package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film changeFilm(Film film);

    Film getFilmById(int id);

    List<Film> getAllFilms();

    String addLike(int filmId, int userId);

    String deleteLike(int filmId, int userId);

    public List<Mpa> getAllMpa();

    public Mpa getMpaById(int id);

    public List<Genres> getAllGenres();

    public Genres getGenreById(int id);
}
