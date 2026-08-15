# java-filmorate
![схема базы данных](./src/main/resources/images/ER-diagram2.png)

## Основные команды БД:


### 1. Получить основную информацию о фильме

```SQL
SELECT name, description, release_date, duration 
FROM Film 
WHERE id = {id вашего фильма}; 
```


### 2. Получить топ 10 фильмов по лайкам 

```SQL
SELECT f.name, COUNT(fl.user_id) AS likes 
FROM Film AS f 
LEFT JOIN FilmLikes AS fl ON fl.film_id = f.id
GROUP BY f.id, f.name 
ORDER BY likes DESC 
LIMIT 10;
```

### 3. Получить возрастной рейтинг фильма 

```SQL
SELECT mpa.age_rating 
FROM Film as f 
JOIN MPA AS mpa ON f.mpa_id = mpa.id 
WHERE f.id = {id вашего фильма};
```

### 4. Получить жанры фильма 

```SQL
SELECT g.genre 
FROM FilmGenres AS fg 
JOIN Genre AS g ON fg.genre_id = g.id 
WHERE fg.film_id = {id вашего фильма};
```

### 5. Получить основную информацию о пользователе 

```SQL
SELECT email, login, name, birthday 
FROM User 
WHERE id = {id нашего юзера};
```


### 6. Получить друзей пользователя 

```SQL
SELECT friend_id 
FROM UserFriends AS uf 
JOIN FriendshipStatus AS fs ON fs.id = uf.friendship_status_id 
WHERE user_id = {id нашего юзера} AND fs.status = 'CONFIRMED';
```

