package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {
    public static User toModel(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .login(userDTO.getLogin())
                .name(userDTO.getName())
                .birthday(userDTO.getBirthday())
                .build();
    }

    public static UserDTO toDto(User model) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(model.getId());
        userDTO.setEmail(model.getEmail());
        userDTO.setLogin(model.getLogin());
        userDTO.setName(model.getName());
        userDTO.setBirthday(model.getBirthday());
        return userDTO;
    }
}
