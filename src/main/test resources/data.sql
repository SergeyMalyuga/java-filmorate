INSERT INTO USERS (USER_ID,  LOGIN, EMAIL, BIRTHDAY , USER_NAME )
VALUES (1, 'Star', 'galya@yandex.ru', '1991-01-25', 'Galya'),
       (2, 'King', 'nick@yandex.ru', '1976-09-20', 'Nick'),
       (3, 'Gold', 'mary@yandex.ru', '1989-11-17', 'Mary');

INSERT INTO RATING (RATING_ID,  RATING_NAME)
VALUES (1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'),(5, 'NC-17');

INSERT INTO GENRE (GENRE_ID, GENRE_NAME)
VALUES (1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'), (4, 'Триллер'), (5, 'Документальный'), (6, 'Боевик');

INSERT INTO FILMS (FILM_ID,  RATING_ID, FILM_NAME, RELEASE_DATE , DURATION)
VALUES (1, 2, 'Kill Bill', '1980-09-20', 1000),
       (2, 2, 'Мстители', '1980-09-20', 1000);

INSERT INTO FRIENDSHIP (FRIENDSHIP_ID, FRIENDSHIP_STATUS)
VALUES (0, 'Не подтверждён'),
       (1, 'Подтверждён');
