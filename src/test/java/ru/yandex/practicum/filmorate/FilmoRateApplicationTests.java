package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void findUserById() {

        Optional<User> userOptional = Optional.of(userStorage.getUserById(3));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 3)
                );
    }

    @Test
    public void addChangeUser() {
        User user = userStorage.getUserById(3);
        user.setName("Три Мушкетёра");
        userStorage.addChangeUser(user);
        User updateUserFromDb = userStorage.getUserById(3);
        assertThat(updateUserFromDb.getName()).isEqualTo("Три Мушкетёра");
    }

    @Test
    public void getAllUsers() {
        List<User> allUsers = userStorage.getAllUsers();
        assertThat(!allUsers.isEmpty());
    }

    @Test
    public void addUser() {
        User user = User.builder()
                .id(1)
                .name("Gosha")
                .login("Puskin1983")
                .email("gosha@maol.ru")
                .birthday(LocalDate.of(1983, 03, 03))
                .build();
        userStorage.addUser(user);
        User userFromDb = userStorage.getUserById(4);
        assertThat(userFromDb).isEqualTo(user);
    }

    @Test
    public void addFriendsToUser() {
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(2, 1);
        User user = userStorage.getUserById(1);
        User user2 = userStorage.getUserById(2);
        assertThat(user.getFriends().get(2)).isEqualTo(FriendshipStatus.CONFIRMED);
        assertThat(user2.getFriends().get(1)).isEqualTo(FriendshipStatus.CONFIRMED);
    }

    @Test
    public void deleteFromFriends() {
        userStorage.addToFriends(1, 2);
        userStorage.addToFriends(2, 1);
        userStorage.deleteFromFriends(1, 2);
        assertThat(userStorage.getUserById(1).getFriends()).isEmpty();
        assertThat(userStorage.getUserById(2).getFriends().get(1)).isEqualTo(FriendshipStatus.UNCONFIRMED);
    }

    @Test
    public void findFilmById() {
        assertThat(filmDbStorage.getFilmById(2).getId()).isEqualTo(2);
        assertThat(filmDbStorage.getFilmById(2).getName()).isEqualTo("Мстители");
    }

    @Test
    public void addFilm() {
        Mpa mpa = new Mpa();
        mpa.setId(2);
        mpa.setName("PG");
        Film film = Film.builder()
                .name("Унесённые призраками")
                .releaseDate(LocalDate.of(1989, 12, 03))
                .mpa(mpa)
                .description("dsdsds")
                .duration(Duration.ofMinutes(1000))
                .build();
        filmDbStorage.addFilm(film);
        Film filmFromDb = filmDbStorage.getFilmById(3);
        assertThat(film).isEqualTo(filmFromDb);
    }

    @Test
    public void changeFilm() {
        Film filmFromDb = filmDbStorage.getFilmById(1);
        filmFromDb.setName("Ходячий замок");
        filmDbStorage.changeFilm(filmFromDb);
        assertThat(filmDbStorage.getFilmById(1).getName()).isEqualTo("Ходячий замок");
    }

    @Test
    public void getAllFilms() {
        List<Film> allFilms = filmDbStorage.getAllFilms();
        assertThat(!allFilms.isEmpty());
    }

    @Test
    public void addLike() {
        filmDbStorage.addLike(2, 3);
        Film filmUpdate = filmDbStorage.getFilmById(2);
        assertThat(filmUpdate.getUserLikes()).contains(3);
    }

    @Test
    public void deleteLike() {
        filmDbStorage.addLike(2, 1);
        filmDbStorage.deleteLike(2, 1);
        Film film = filmDbStorage.getFilmById(2);
        assertThat(film.getUserLikes()).noneMatch(p -> p == 1);
    }

    @Test
    public void getAllMpa() {
        assertThat(filmDbStorage.getAllMpa()).isNotEmpty();
    }

    @Test
    public void getMpaById() {
        Mpa mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("G");
        assertThat(filmDbStorage.getMpaById(1)).isEqualTo(mpa);
    }

    @Test
    public void shouldGetAllGenres() {
        assertThat(filmDbStorage.getAllGenres()).isNotEmpty();
    }

    @Test
    public void shouldGetGenresById() {
        assertThat(filmDbStorage.getGenreById(1).getName()).isEqualTo(GenreType.Комедия);
    }
}
