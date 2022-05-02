package ru.rakhcheev.tasket.api.tasketapi.exception;

public class UserDatabaseIsEmptyException extends Exception {
    public UserDatabaseIsEmptyException(String message) {
        super(message);
    }
}
