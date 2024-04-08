package ru.yandex.practicum.filmorate.exception;

public class MpaBadRequestException extends RuntimeException {
    public MpaBadRequestException(String message) {
        super(message);
    }
}