package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
public class DirectorService {
    private final DirectorStorage directorStorage;

    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public DirectorDTO findById(final Long id) {
        Director director = directorStorage.findById(id);

        if (director == null) {
            throw new NotFoundException("Director with id " + id + " not found");
        }

        return DirectorMapper.toDto(director);
    }

    public List<DirectorDTO> getAll() {
        return directorStorage
                .getAll()
                .stream()
                .map(DirectorMapper::toDto)
                .toList();
    }

    public DirectorDTO create(final DirectorDTO directorDto) {
        validate(directorDto);

        Director director = DirectorMapper.toModel(directorDto);

        director = directorStorage.create(director);

        directorDto.setId(director.getId());

        return directorDto;
    }

    public DirectorDTO update(final DirectorDTO directorDto) {
        Director director = directorStorage.findById(directorDto.getId());

        if (director == null) {
            throw new NotFoundException("Director with id " + directorDto.getId() + " not found");
        }

        director.setName(directorDto.getName());

        directorStorage.update(director);

        return directorDto;
    }

    public void delete(final Long id) {
        directorStorage.deleteById(id);
    }

    private void validate(final DirectorDTO directorDto) {
        if (directorDto.getName() == null || directorDto.getName().isEmpty()) {
            throw new ValidationException("Director name is empty");
        }
    }

}
