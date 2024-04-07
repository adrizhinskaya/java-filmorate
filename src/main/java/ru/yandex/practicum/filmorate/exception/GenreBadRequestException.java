package ru.yandex.practicum.filmorate.exception;

public class GenreBadRequestException extends RuntimeException {
    public GenreBadRequestException(String message) {
        super(message);
    }
}