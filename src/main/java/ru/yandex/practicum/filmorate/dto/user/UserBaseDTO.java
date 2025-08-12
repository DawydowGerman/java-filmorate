package ru.yandex.practicum.filmorate.dto.user;

import java.time.LocalDate;
import java.util.Set;

public interface UserBaseDTO {
    String getEmail();
    String getLogin();
    String getName();
    LocalDate getBirthday();
    Set<Long> getFriends();
    void setName(String name);
    void setFriends(Set<Long> friends);
}