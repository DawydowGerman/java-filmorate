package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.MpaDTO;
import ru.yandex.practicum.filmorate.model.Mpa;

public class MpaMapper {
    public static Mpa toModel(MpaDTO mpaDTO) {
        Mpa mpa = new Mpa(mpaDTO.getId(), mpaDTO.getName());
        return mpa;
    }

    public static MpaDTO toDTO(Mpa mpa) {
        MpaDTO mpaDTO = new MpaDTO(mpa.getId(), mpa.getName());
        return mpaDTO;
    }
}