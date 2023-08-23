package ru.yandex.practicum.filmorate.exception;

public class DataByIdException extends RuntimeException {
    public DataByIdException(String message){
        super(message);
    }
}
