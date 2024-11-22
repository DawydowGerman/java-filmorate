package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.*;

@Data
@Builder
public class User {
    @Builder.Default
    private Long id = Long.valueOf(0);
    @Builder.Default
    private String email = "Default email";
    @Builder.Default
    private String login = "Default login";
    @Builder.Default
    private String name = "Default name";
    @Builder.Default
    private LocalDate birthday = LocalDate.of(2000, 12, 01);
    @Builder.Default
    private Set<Long> friends = new HashSet<>();
    private UserService userService;
    private FilmService filmService;

    public void addFriend(Long addingFriendId) {
        userService.addFriend(this.id, addingFriendId);
    }

    public void removeFriend(Long removingFriendId) {
        userService.removeFriend(this.id, removingFriendId);
    }

    public List<User> getMutualFriends(Long mutualFriendId) {
        return userService.getMutualFriends(this.id, mutualFriendId);
    }

    public void setToFriends(Long id) {
        friends.add(id);
    }

    public void removeFromFriends(Long id) {
        friends.remove(id);
    }

    public boolean isFriend(Long id) {
        return friends.contains(id);
    }

    public void giveLike(Long filmId) {
        filmService.giveLike(this.id, filmId);
    }

    public void removeLike(Long filmId) {
        filmService.removeLike(this.id, filmId);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        return filmService.getMostPopularFilms(count);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", friends=" + friends +
                '}';
    }
}
