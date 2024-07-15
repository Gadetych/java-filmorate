package ru.yandex.practicum.filmorate.exception;

public class IncorrectGenreException extends RuntimeException {
    public IncorrectGenreException(String message) {
        super(message);
    }

    public IncorrectGenreException(String message, Throwable cause) {
        super(message, cause);
    }
}
