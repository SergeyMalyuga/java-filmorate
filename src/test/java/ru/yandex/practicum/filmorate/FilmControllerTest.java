package ru.yandex.practicum.filmorate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class FilmControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetAllFilms() {

        restTemplate.postForEntity("/films", Film.builder()
                .name("От заката до рассвета")
                .description("Ужасы")
                .duration(Duration.ofMinutes(140))
                .releaseDate(LocalDate.of(1996, 01, 01))
                .build(), Film.class);
        restTemplate.postForEntity("/films", Film.builder()
                .name("Gone")
                .description("Мистика")
                .duration(Duration.ofMinutes(140))
                .releaseDate(LocalDate.of(1985, 12, 28))
                .build(), Film.class);
        ResponseEntity<List<Film>> response = restTemplate.exchange("/films", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Film>>() {
                });
        List<Film> films = response.getBody();
        assertThat(films).isNotEmpty();

    }

    @Test
    public void shoulAddFilm() {
        Film film = Film.builder()
                .name("Gone")
                .description("Horor")
                .duration(Duration.ofMinutes(140))
                .releaseDate(LocalDate.now())
                .build();

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        assertThat(response.getBody().equals(film));
    }

    @Test
    public void shouldUpdateFilm() {
        Film film = Film.builder()
                .id(1)
                .name("Джанго освобожденный")
                .description("Вестерн")
                .duration(Duration.ofMinutes(140))
                .releaseDate(LocalDate.now())
                .build();
        System.out.println(film.getId());
        ResponseEntity<Film> responsePost = restTemplate.postForEntity("/films", film, Film.class);
        assertThat(responsePost.getBody().equals(film));
        film.setName("Джанго в Анапе");
        HttpEntity<Film> entity = new HttpEntity<>(film);
        responsePost = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);
        ResponseEntity<List<Film>> response = restTemplate.exchange("/films", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Film>>() {
                });
        List<Film> filmsList = response.getBody();
        assertThat(filmsList.get(0).getName()).isEqualTo("Джанго в Анапе");
        assertThat(filmsList.get(0)).isEqualTo(film);
    }
}



