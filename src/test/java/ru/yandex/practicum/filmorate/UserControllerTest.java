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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetAllUsers() {
        restTemplate.postForEntity("/users", User.builder()
                .login("Kventin")
                .name("Tarantino")
                .email("ser@gmail.com")
                .birthday(LocalDate.of(1978, 01, 01))
                .build(), User.class);
        ResponseEntity<List<User>> response = restTemplate.exchange("/users", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });
        List<User> usersList = response.getBody();
        assertThat(usersList).isNotEmpty();
    }

    @Test
    public void shouldAddUser() {
        User user = User.builder()
                .login("Kventin")
                .name("Tarantino")
                .email("ser@gmail.com")
                .birthday(LocalDate.of(1978, 01, 01))
                .build();
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(user.getName());
    }

    @Test
    public void shouldUpdateUser() {
        User user = User.builder()
                .id(1)
                .login("Kventin")
                .name("Tarantino")
                .email("ser@gmail.com")
                .birthday(LocalDate.of(1978, 01, 01))
                .build();
        ResponseEntity<User> responsePost = restTemplate.postForEntity("/users", user, User.class);

        assertThat(responsePost.getBody()).isEqualTo(user);
        user.setName("Марина");
        HttpEntity<User> entity = new HttpEntity<>(user);
        restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);
        ResponseEntity<List<User>> response = restTemplate.exchange("/users", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });
        List<User> usersList = response.getBody();
        assertThat(response.getBody().get(0).getName()).isEqualTo("Марина");
        assertThat(response.getBody().get(0)).isEqualTo(user);
    }
}