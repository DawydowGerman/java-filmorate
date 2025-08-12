package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserRequestDTO implements UserBaseDTO {
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setFriends(Set<Long> friends) {
        this.friends = friends;
    }
}