package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.film.mappers.GenreRowMapper;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class FilmDBStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;



    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("release_date", film.getReleaseDate());
        parameters.put("duration", film.getDuration());
        parameters.put("mpa_id", film.getMpa().getId());

        Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
        film.setId(id.longValue());

        saveGenres(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql =
                "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        saveGenres(film);
        return film;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM films WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public Optional<Film> findOne(Long id) {
        String sql = "SELECT * FROM films WHERE id = ?";
        List<Film> films =
                jdbcTemplate.query(sql,
                        new FilmRowMapper(), id);
        if (films.isEmpty()) {
            return Optional.empty();
        }
        Film film = films.getFirst();
        film.setGenres(new HashSet<>(getFilmGenresById(id)));
        film.setLikes(new HashSet<>(getFilmLikesById(id)));
        return Optional.of(film);
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(sql, new FilmRowMapper());
        for (Film film: films) {
            film.setGenres(new HashSet<>(getFilmGenresById(film.getId())));
            film.setLikes(new HashSet<>(getFilmLikesById(film.getId())));
        }
        return films;
    }

    @Override
    public Film addLike(Long id, Long userId) {
        String sql = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, userId);
        return findOne(id).orElseThrow(() -> new NotFoundException("id", "Film hasn't been found"));
    }

    @Override
    public boolean deleteLike(Long id, Long userId) {
        String sql = "DELETE FROM films_likes WHERE film_id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public Collection<Genre> getFilmGenresById(Long id) {
        String sql = "SELECT genre_id FROM films_genres WHERE film_id = ?";
        return jdbcTemplate.query(sql,
                new GenreRowMapper(), id);
    }

    public Collection<Long> getFilmLikesById(Long id) {
        String sql = "SELECT user_id FROM films_likes WHERE film_id = ?";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getLong("user_id"), id);
    }

    public void saveGenres(Film film) {
        String sqlDelete = "DELETE FROM films_genres WHERE film_id = ?";
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        jdbcTemplate.update(sqlDelete, film.getId());
        Collection<Genre> genres = film.getGenres();
        String sqlUpdate = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sqlUpdate,
                genres, genres.size(), (ps, genre) -> {
            ps.setLong(1, film.getId());
            ps.setLong(2, genre.getId());
        });
    }


}
