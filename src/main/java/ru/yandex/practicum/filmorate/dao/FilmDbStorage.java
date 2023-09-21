package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataByIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreType;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Component
@Slf4j

public class FilmDbStorage implements FilmStorage {
    JdbcTemplate jdbcTemplate;
    private static int count = 0;
    private int ratingId;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlSetGenre = "INSERT INTO films_by_genres(film_id, genre_id) VALUES(?,?)";
        String sql = "INSERT INTO films(film_id, rating_id, film_name,description, release_date, duration)" +
                " VALUES(?,?,?,?,?,?)";
        validationCheckFilm(film);
        film.setId(setCountId());

        jdbcTemplate.update(sql, film.getId(), film.getMpa().getId(), film.getName(),
                film.getDescription(), film.getReleaseDate(),
                film.getDuration());

        List<Genres> genreType = new ArrayList<>(film.getGenres());
        for (Genres genre : genreType) {
            jdbcTemplate.update(sqlSetGenre, film.getId(), genre.getId());
        }
        log.info("Фильм добавлен!");
        return film;
    }

    @Override
    public Film changeFilm(Film film) {
        validationCheckFilm(film);
        String sql = "UPDATE films SET rating_id=?, film_name=?, description=?, release_date =?, " +
                "duration=? WHERE film_id = ?";
        String sqlGenreUpdate = "INSERT INTO films_by_genres (film_id, genre_id) VALUES(?,?)";
        String sqlGenreDelete = "DELETE FROM films_by_genres WHERE film_id = ?";

        getFilmById(film.getId());

        jdbcTemplate.update(sql, film.getMpa().getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getId());

        if (!film.getGenres().isEmpty()) {
            TreeSet<Genres> genres = film.getGenres();
            jdbcTemplate.update(sqlGenreDelete, film.getId());
            for (Genres genre : genres) {
                jdbcTemplate.update(sqlGenreUpdate, film.getId(), genre.getId());
            }
        } else {
            jdbcTemplate.update(sqlGenreDelete, film.getId());
        }
        log.info("Данные о фильме изменены!");
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT * FROM films WHERE film_id=?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        Film film;
        if (sqlRowSet.next()) {
            film = jdbcTemplate.queryForObject(sql, (rs, inF) -> makeFilm(rs), id);
        } else {
            log.info("Фильма с id: {} не существует!", id);
            throw new DataByIdException("Фильма с id: " + id + " не существует!");
        }
        return addUserLikes(addGenreToFilm(film));
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM films";
        log.info("Список фильмов получен.");
        List<Film> filmsList = (jdbcTemplate.query(sql, (rs, inF) -> makeFilm(rs)));
        for (Film film : filmsList) {
            addUserLikes(addGenreToFilm(film));
        }
        return filmsList;
    }

    @Override
    public String addLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        film.getUserLikes().add(userId);
        String sql = "INSERT INTO user_likes(like_id, user_id, film_id) VALUES (?,?,?);";
        jdbcTemplate.update(sql, count++, userId, film.getId());
        return "Вы поставили like фильму: " + film.getName();
    }

    @Override
    public String deleteLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        String sql = "DELETE FROM user_likes WHERE user_id = ?;";
        String sqlCheckId = "SELECT * FROM user_likes WHERE user_id = ? AND film_id = ?;";
        List<Integer> userLikes = jdbcTemplate.query(sqlCheckId, (rs, rowNum) -> makeUserLikes(rs), userId, filmId);
        if (!userLikes.isEmpty()) {
            film.getUserLikes().remove(userId);
            jdbcTemplate.update(sql, userId);
            return "Лайк с фильма: " + film.getName() + " удалён: ";
        }
        throw new DataByIdException("Вы не лайкали: " + film.getName());
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM rating";
        List<Mpa> allMpaList = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            Mpa mpa = new Mpa();
            mpa.setId(sqlRowSet.getInt("rating_id"));
            mpa.setName(getRatingTypeById(sqlRowSet.getInt("rating_id")));
            allMpaList.add(mpa);
        }
        return allMpaList;
    }

    @Override
    public Mpa getMpaById(int id) {
        String sql = "SELECT * FROM rating WHERE rating_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        Mpa mpa;
        if (sqlRowSet.next()) {
            mpa = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeMpa(rs), id);
        } else {
            throw new DataByIdException("Рейтинг с id: " + id + " не найден!");
        }
        return mpa;
    }

    @Override
    public List<Genres> getAllGenres() {
        String sql = "SELECT * FROM genre";
        List<Genres> allGenres = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            Genres genre = new Genres();
            genre.setId(sqlRowSet.getInt("genre_id"));
            genre.setName(getGenreTypeById(genre.getId()));
            allGenres.add(genre);
        }
        return allGenres;
    }

    public Genres getGenreById(int id) {
        Genres genre = new Genres();
        if (getGenreTypeById(id) != null) {
            genre.setId(id);
            genre.setName(getGenreTypeById(id));
        } else {
            throw new DataByIdException("Жанра с id: " + id + "не существует.");
        }
        return genre;
    }

    private String getRatingTypeById(int id) {
        switch (id) {
            case 1:
                return "G";
            case 2:
                return "PG";
            case 3:
                return "PG-13";
            case 4:
                return "R";
            case 5:
                return "NC-17";
        }
        return "NO_TYPE";
    }

    private GenreType getGenreTypeById(int id) {
        switch (id) {
            case 1:
                return GenreType.Комедия;
            case 2:
                return GenreType.Драма;
            case 3:
                return GenreType.Мультфильм;
            case 4:
                return GenreType.Триллер;
            case 5:
                return GenreType.Документальный;
            case 6:
                return GenreType.Боевик;
        }
        return null;
    }


    private Film makeFilm(ResultSet rs) throws SQLException {
        int filmId = rs.getInt("film_id");
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("rating_id"));
        mpa.setName(getRatingTypeById(rs.getInt("rating_id")));
        String filmName = rs.getString("film_name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Duration duration = Duration.ofSeconds(rs.getInt("duration"));
        return Film.builder().id(filmId)
                .mpa(mpa)
                .name(filmName)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration).build();
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("rating_id");
        String name = rs.getString("rating_name");
        Mpa mpa = new Mpa();
        mpa.setId(id);
        mpa.setName(name);
        return mpa;
    }

    private Film addGenreToFilm(Film film) {
        String sql = "SELECT genre_id FROM films_by_genres WHERE film_id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, film.getId());
        while (sqlRowSet.next()) {
            Genres genre = new Genres();
            genre.setId(sqlRowSet.getInt("genre_id"));
            genre.setName(getGenreTypeById(sqlRowSet.getInt("genre_id")));
            film.getGenres().add(genre);
        }
        return film;
    }

    private Film addUserLikes(Film film) {
        String sql = "SELECT user_id FROM user_likes WHERE film_id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, film.getId());
        while (sqlRowSet.next()) {
            film.getUserLikes().add(sqlRowSet.getInt("user_id"));
        }
        return film;
    }

    private int makeUserLikes(ResultSet rs) throws SQLException {
        return rs.getInt("film_id");
    }

    private int setCountId() {
        if (getAllFilms().size() == 0) {
            return 1;
        }
        return getAllFilms().size() + 1;
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

