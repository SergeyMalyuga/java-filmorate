package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class FilmService {
    private FilmStorage filmStorage;
   @Autowired
    public FilmService(FilmStorage filmStorage){
        this.filmStorage = filmStorage;
    }

    public void addLike(Film film, User user){
       film.getUserLikes().add(user.getId());
    }

    public void deleteLike(Film film, User user){ //TODO если не правильный user (нет лайка)
        if (film.getUserLikes().contains(user.getId())) {
            film.getUserLikes().remove(user.getId());
        }
    }

    public List<Film> getPopularFilms(List<Film> film){
        return film.stream().sorted((f1,f2) -> f2.getUserLikes().size() - f1.getUserLikes().size())
                .limit(10)
                .collect(Collectors.toList());
    }
}
