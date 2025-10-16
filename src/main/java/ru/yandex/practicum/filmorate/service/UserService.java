package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.user.UserBaseDTO;
import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.dto.user.UserRequestDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;

@Service
@Data
public class UserService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final DatabaseFilmGenresStorage databaseFilmGenresStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final FriendshipStorage friendshipStorage;
    private final EventService eventService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserService(@Qualifier("DatabaseFilmStorage") FilmStorage filmStorage,
                       @Qualifier("DatabaseUserStorage") UserStorage userStorage,
                       @Qualifier("DatabaseMpaStorage") MpaStorage mpaStorage,
                       @Qualifier("DatabaseGenreStorage") GenreStorage genreStorage,
                       DatabaseFilmGenresStorage databaseFilmGenresStorage,
                       @Qualifier("DatabaseFriendshipStorage") FriendshipStorage friendshipStorage,
                       EventService eventService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.databaseFilmGenresStorage = databaseFilmGenresStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.friendshipStorage = friendshipStorage;
        this.eventService = eventService;
    }

    public UserDTO create(UserRequestDTO userDto) {
        userStorage.findAll()
                .filter(list -> !list.isEmpty())
                .ifPresent(users -> users.stream()
                        .filter(user -> user.getEmail().equals(userDto.getEmail()))
                        .findFirst()
                        .ifPresent(user -> {
                            log.error("Ошибка при добавлении юзера");
                            throw new ValidationException("Этот имейл уже используется");
                        }));
        UserRequestDTO userDTO = this.validateUserDTO(userDto);
        User user = UserMapper.toModelCreate(userDTO);
        user = userStorage.create(user);
        return UserMapper.toDto(user);
    }

    public UserDTO getUserById(Long userId) {
        return userStorage.getUserById(userId)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
    }

    public List<UserDTO> findAll() {
        return userStorage.findAll()
                .orElseThrow(() -> new NotFoundException("Список юзеров пуст."))
                .stream()
                .map(user -> UserMapper.toDto(user))
                .collect(Collectors.toList());
    }

    public UserDTO update(UserDTO userDto) {
        if (userDto.getId() == null) {
            log.error("Ошибка при обновлении данных юзера");
            throw new ValidationException("Id должен быть указан");
        }
        if (!userStorage.isUserIdExists(userDto.getId())) {
            log.error("Ошибка при обновлении данных юзера");
            throw new NotFoundException("Юзер отсутствуют");
        }
        userStorage.findAll()
                .orElseThrow(() -> new NotFoundException("Список юзеров пуст."))
                .stream()
                .filter(user0 ->
                        !user0.getId().equals(userDto.getId()) &&
                                user0.getEmail().equals(userDto.getEmail()))
                .findFirst()
                .ifPresent(user0 -> {
                    log.error("Ошибка при обновлении данных юзера");
                    throw new ValidationException("Этот имейл уже используется");
                });
        UserDTO userDTO = this.validateUserDTO(userDto);
        User user0 = UserMapper.toModelUpdate(userDTO);
        user0 = userStorage.update(user0);
        return UserMapper.toDto(user0);
    }

    public void addFriend(Long id, Long friendId) {
        if (!userStorage.isUserIdExists(id) || !userStorage.isUserIdExists(friendId)) {
            log.error("Ошибка при добавлении в друзья");
            throw new NotFoundException("Один из юзеров либо оба отсутствуют");
        }
        if (id.equals(friendId)) {
            throw new ValidationException("Юзер не может добавить в друзья самого себя");
        }
        if (friendshipStorage.isFriendshipExists(id, friendId)) {
            log.error("Ошибка при добавлении в друзья");
            throw new ValidationException("Юзер уже добавлен в друзья");
        }
        friendshipStorage.addFriend(id, friendId);
        eventService.add(friendId, id, EventType.FRIEND);
    }

    public void removeFriend(Long id, Long friendId) {
        if (!userStorage.isUserIdExists(id) || !userStorage.isUserIdExists(friendId)) {
            log.error("Ошибка при удалении из друзей");
            throw new NotFoundException("Один из юзеров либо оба отсутствуют");
        }
        if (id.equals(friendId)) {
            throw new ValidationException("Юзер не может удалить из друзей самого себя");
        }
        if (!friendshipStorage.isFriendshipExists(id, friendId)) {
            log.error("Ошибка при добавлении в друзья");
            throw new ValidationException("Юзеры не являются друзьями");
        }
        friendshipStorage.removeFriend(id, friendId);
        eventService.remove(friendId, id, EventType.FRIEND);
    }

    public List<UserDTO> getFriends(Long id) {
        if (!userStorage.isUserIdExists(id)) {
            log.error("Ошибка при добавлении в друзья");
            throw new NotFoundException("Юзер с " + id + " отсутствует.");
        }
        return friendshipStorage.getFriends(id)
                .map(friends -> friends.stream()
                        .map(UserMapper::toDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public List<UserDTO> getMutualFriends(Long idUser0, Long idUser1) {
        if (!userStorage.isUserIdExists(idUser0) || !userStorage.isUserIdExists(idUser1)) {
            log.error("Ошибка при удалении из друзей");
            throw new NotFoundException("Один из юзеров либо оба отсутствуют");
        }

        Optional<List<User>> optionalUserList = friendshipStorage.getMutualFriends(idUser0, idUser1);
        return optionalUserList.map(users -> users
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList())).orElseGet(ArrayList::new);
    }

    public List<FilmDTO> getRecommendations(Long userId) {
        return filmStorage.getRecommendations(userId)
                .orElseThrow(() -> new NotFoundException("Список фильмов пуст."))
                .stream()
                .map(film -> {
                    if (databaseFilmGenresStorage.isFilmHasGenre(film.getId())) {
                        this.assignGenres(film);
                    }
                    this.assignMpa(film);
                    return FilmMapper.toDto(film);
                 })
                .collect(Collectors.toList());
    }

    public void remove(Long id) {
        if (!userStorage.isUserIdExists(id)) {
            log.error("Ошибка при удалении юзера с id = {}", id);
            throw new NotFoundException("Юзер не найден с id = " + id);
        }
        userStorage.remove(id);
    }

    private Film assignGenres(Film film) {
        List<Long> genresList = databaseFilmGenresStorage.getGenresIdsOfFilm(film.getId());
        List<Genre> filmGenresList = genreStorage.findAll()
                .stream()
                .filter(genre -> genresList.contains(genre.getId()))
                .toList();
        film.setGenres(filmGenresList);
        return film;
    }

    private Film assignMpa(Film film) {
        Optional<Mpa> optionalMpa = mpaStorage.getById(film.getMpa().getId());
        optionalMpa.ifPresent(mpa -> film.getMpa().setName(mpa.getName()));
        return film;
    }

    private <T extends UserBaseDTO> T validateUserDTO(T userDTO) {
        if (userDTO.getEmail() == null || userDTO.getEmail().isBlank() || !userDTO.getEmail().contains("@")) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (userDTO.getLogin() == null || userDTO.getLogin().isBlank() || userDTO.getLogin().contains(" ")) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (userDTO.getBirthday() == null) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Дата рождения должна быть указана");
        } else if (userDTO.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка при добавлении юзера");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (userDTO.getName() == null || userDTO.getName().isBlank()) {
            userDTO.setName(userDTO.getLogin());
        }
        if (userDTO.getFriends() == null) {
            userDTO.setFriends(new HashSet<>());
        }
       return userDTO;
    }
}