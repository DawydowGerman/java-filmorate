package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.dto.user.UserRequestDTO;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {
    public static User toModelCreate(UserRequestDTO userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .login(userDTO.getLogin())
                .name(userDTO.getName())
                .birthday(userDTO.getBirthday())
                .build();
    }

    public static User toModelUpdate(UserDTO userDTO) {
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