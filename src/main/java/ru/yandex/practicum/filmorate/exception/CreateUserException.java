package ru.yandex.practicum.filmorate.exception;

public class CreateUserException extends RuntimeException {
    public CreateUserException(String message) {
        super(message);
    }

    public CreateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
