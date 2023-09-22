package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Genres implements Serializable, Comparable<Genres> {
    private int id;
    private String name;

    @Override
    public int compareTo(Genres o) {
        return this.id - o.id;
    }
}
