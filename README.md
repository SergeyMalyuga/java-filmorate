# java-filmorate
Template repository for Filmorate project.

![Filmorate.png](src/main/resources/Filmorate.png)

### Код для запросов:
1. Запрос друзей для определённого user с отображением статуса friendsgip (подтверждён/не подтверждён).
``` sql
SELECT
uf.first_name,
uf.last_name,
fr.status
FROM user AS u
INNER JOIN friends AS f ON u.user_id = f.user_id
INNER JOIN user AS uf ON f.user_friend_id = uf.user_id
INNER JOIN friendship AS fr ON f.friendship_id = fr.friendship_id
WHERE
u.user_id = 1 

```
***
2. Количество поставленных лайков.
```
SELECT
f.name,
COUNT(ul.like_id)
FROM film AS f
INNER JOIN user_likes AS ul ON f.film_id = ul.film_id
GROUP BY
f.name
```
***
3. Рейтинг фильмов.
```
SELECT
f.name,
g.name,
r.name
FROM film AS f
INNER JOIN film_by_genres AS fg ON f.film_id = fg.film_id
INNER JOIN rating AS r ON f.rating_id = r.rating_id
INNER JOIN genre AS g ON fg.genre_id = g.genre_id
GROUP BY
f.name
```
***
4. Топ 5 фильмов
```
SELECT
f.name,
COUNT(ul.like_id)
FROM film AS f
INNER JOIN user_likes AS ul ON f.film_id = ul.film_id
GROUP BY
f.name
ORDER BY
COUNT(ul.like_id) DESC
LIMIT 5;
```