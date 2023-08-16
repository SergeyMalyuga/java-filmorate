package ru.yandex.practicum.filmorate.controller.exception;


import java.io.IOException;
public class ValidationException extends RuntimeException {
    public ValidationException(String string){
        super(string);
    }
}
