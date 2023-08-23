package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.getFilmStorage().addFilm(film);
    }

    @PutMapping()
    public Film changeFilm(@Valid @RequestBody Film film) {
        return filmService.getFilmStorage().changeFilm(film);
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        return filmService.getFilmStorage().getAllFilms();
    }

    @GetMapping("{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmStorage().getFilmById(id);
    }
}