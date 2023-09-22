package ru.yandex.practicum.filmorate.model;

public enum GenreType {
    COMEDY("Комедия"),
    DRAMA("Драма"),
    CARTOON("Мультфильм"),
    THRILLER("Триллер"),
    DOCUMENTARY("Документальный"),
    ACTION_MOVIE("Боевик");

    private String value;

    GenreType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
