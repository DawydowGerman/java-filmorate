package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.enums.MpaRating;

@Data
public class Mpa {
    private Long id;
//    private MpaRating mpaRating;
    private String name;

    public Mpa() {
    }

    public Mpa(Long id) {
        this.id = id;
    }

    public Mpa(String id) {
        this.id = Long.parseLong(id);
    }

    public Mpa(Long id, String mpaRating) {
        this.id = id;
        this.name = mpaRating;
        // this.mpaRating = MpaRating.valueOf(mpaRating);
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMpaRating() {
        return name;
    }

    public void setMpaRating(String mpaRating) {
        this.name = mpaRating;
    }


    /*
    public MpaRating getMpaRating() {
        return mpaRating;
    }

    public void setMpaRating(MpaRating mpaRating) {
        this.mpaRating = mpaRating;
    }

     */
}