package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dto.NewMPARequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDBStorage;
import ru.yandex.practicum.filmorate.storage.film.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDBStorage;
import ru.yandex.practicum.filmorate.storage.user.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDBStorage.class, UserRowMapper.class, FilmDBStorage.class, FilmRowMapper.class})
class FilmoRateApplicationTests {
    private final UserDBStorage userStorage;
    private final FilmDBStorage filmStorage;

    @Test
    public void testAddUser() {
        User newUser = userStorage.add(createUser("email@mail.ru","login"));
        Assertions.assertEquals(newUser, userStorage.findOne(newUser.getId()).get());
    }

    @Test
    public void testFindUserById() {
        User newUser = userStorage.add(createUser("email@mail.ru","login"));

        Optional<User> userOptional = userStorage.findOne(newUser.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", newUser.getId())
                );
    }

    @Test
    public void testFindUsers() {
        userStorage.add(createUser("email@mail.ru","login"));
        userStorage.add(createUser("emailOther@mail.ru","login228"));

        Assertions.assertEquals(2, userStorage.findAll().size());
    }

    @Test
    public void testDeleteUser() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        userStorage.delete(user.getId());
        Assertions.assertTrue(userStorage.findAll().isEmpty());
    }

    @Test
    public void testUpdateUser() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setId(user.getId());
        updateUserRequest.setLogin("otherLogin");
        updateUserRequest.setEmail("Otheremail@gmail.com");
        User updatedUser = UserMapper.updateUserFields(user, updateUserRequest);
        userStorage.update(updatedUser);
        Assertions.assertEquals(updatedUser, userStorage.findOne(user.getId()).get());
    }

    @Test
    public void testGetEmptyFriendsList() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        Assertions.assertTrue(userStorage.findFriends(user.getId()).isEmpty());
    }

    @Test
    public void testGetFriendsListWithFriends() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        User user2 = userStorage.add(createUser("emailOther@mail.ru","login228"));

        userStorage.addFriend(user.getId(), user2.getId());
        Assertions.assertEquals(1, userStorage.findFriends(user.getId()).size());
    }

    @Test
    public void testAddFriendUnconfirmed() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        User user2 = userStorage.add(createUser("emailOther@mail.ru","login228"));

        userStorage.addFriend(user.getId(), user2.getId());
        Assertions.assertEquals(FriendshipStatus.UNCONFIRMED, userStorage.findFriends(user.getId()).get(user2.getId()));
    }

    @Test
    public void testAddFriendConfirmed() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        User user2 = userStorage.add(createUser("emailOther@mail.ru","login228"));

        userStorage.addFriend(user.getId(), user2.getId());
        userStorage.addFriend(user2.getId(), user.getId());
        Assertions.assertEquals(FriendshipStatus.CONFIRMED, userStorage.findFriends(user.getId()).get(user2.getId()));
    }

    @Test
    public void testDeleteFriendUnconfirmed() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        User user2 = userStorage.add(createUser("emailOther@mail.ru","login228"));

        userStorage.addFriend(user.getId(), user2.getId());
        userStorage.deleteFriend(user.getId(),user2.getId());
        Assertions.assertTrue(userStorage.findFriends(user.getId()).isEmpty());
    }

    @Test
    public void testDeleteFriendConfirmed() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        User user2 = userStorage.add(createUser("emailOther@mail.ru","login228"));

        userStorage.addFriend(user.getId(), user2.getId());
        userStorage.addFriend(user2.getId(), user.getId());
        userStorage.deleteFriend(user.getId(),user2.getId());
        Assertions.assertEquals(FriendshipStatus.UNCONFIRMED, userStorage.findFriends(user2.getId()).get(user.getId()));
    }

    @Test
    public void testDeleteFriendWithoutFriendRequest() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        User user2 = userStorage.add(createUser("emailOther@mail.ru","login228"));
        userStorage.deleteFriend(user.getId(),user2.getId());

        Assertions.assertTrue(userStorage.findFriends(user.getId()).isEmpty());
    }

    @Test
    public void testAddFilm() {
        Film newFilm = filmStorage.add(createFilm("film"));
        Assertions.assertEquals(newFilm, filmStorage.findOne(newFilm.getId()).get());
    }

    @Test
    public void testFindFilmById() {
        Film newFilm = filmStorage.add(createFilm("film"));

        Optional<Film> filmOptional = filmStorage.findOne(newFilm.getId());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", newFilm.getId())
                );
    }

    @Test
    public void testFindFilms() {
        filmStorage.add(createFilm("film"));
        filmStorage.add(createFilm("film2"));

        Assertions.assertEquals(2, filmStorage.findAll().size());
    }

    @Test
    public void testDeleteFilm() {
        Film newFilm = filmStorage.add(createFilm("film"));
        filmStorage.delete(newFilm.getId());
        Assertions.assertTrue(filmStorage.findAll().isEmpty());
    }

    @Test
    public void testUpdateFilm() {
        Film newFilm = filmStorage.add(createFilm("film"));
        UpdateFilmRequest updateFilmRequest = new UpdateFilmRequest();
        updateFilmRequest.setId(newFilm.getId());
        updateFilmRequest.setName("other film");
        NewMPARequest newMPARequest = new NewMPARequest();
        newMPARequest.setId(5L);
        updateFilmRequest.setMpa(newMPARequest);
        Film updatedFilm = FilmMapper.updateFilmFields(newFilm, updateFilmRequest);
        filmStorage.update(updatedFilm);
        Assertions.assertEquals(updatedFilm, filmStorage.findOne(newFilm.getId()).get());
    }

    @Test
    public void testAddLike() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        Film newFilm = filmStorage.add(createFilm("film"));
        filmStorage.addLike(newFilm.getId(), user.getId());

        Assertions.assertEquals(1, filmStorage.findOne(newFilm.getId()).get().getLikes().size());
    }

    @Test
    public void testAddLikeTwoTimesSameUser() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        Film newFilm = filmStorage.add(createFilm("film"));
        filmStorage.addLike(newFilm.getId(), user.getId());
        filmStorage.addLike(newFilm.getId(), user.getId());

        Assertions.assertEquals(1, filmStorage.findOne(newFilm.getId()).get().getLikes().size());
    }

    @Test
    public void testDeleteLike() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        Film newFilm = filmStorage.add(createFilm("film"));
        filmStorage.addLike(newFilm.getId(), user.getId());
        filmStorage.deleteLike(newFilm.getId(), user.getId());

        Assertions.assertTrue(filmStorage.findOne(newFilm.getId()).get().getLikes().isEmpty());
    }

    @Test
    public void testDeleteLikeTwoTimesSameUser() {
        User user = userStorage.add(createUser("email@mail.ru","login"));
        Film newFilm = filmStorage.add(createFilm("film"));
        filmStorage.addLike(newFilm.getId(), user.getId());
        filmStorage.deleteLike(newFilm.getId(), user.getId());
        filmStorage.deleteLike(newFilm.getId(), user.getId());

        Assertions.assertTrue(filmStorage.findOne(newFilm.getId()).get().getLikes().isEmpty());
    }


    private User createUser(String email, String login) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setBirthday(LocalDate.of(1990, 1, 1));
        return user;
    }

    private Film createFilm(String name) {
        Film film = new Film();
        film.setName(name);
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setMpa(MPA.G);
        film.setGenres(new HashSet<>());
        return film;
    }

}