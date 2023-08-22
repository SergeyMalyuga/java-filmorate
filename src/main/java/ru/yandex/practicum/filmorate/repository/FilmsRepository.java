package ru.yandex.practicum.filmorate.repository;

import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Data
@Repository
public class FilmsRepository<T> {
    private final Map<Integer, T> repositoryList;
    public FilmsRepository() {
    this.repositoryList = new HashMap<>();
    }
}