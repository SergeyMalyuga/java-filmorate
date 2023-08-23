package ru.yandex.practicum.filmorate.repository;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Repository<T> {
    private final Map<Integer, T> repositoryList = new HashMap<>();

}
