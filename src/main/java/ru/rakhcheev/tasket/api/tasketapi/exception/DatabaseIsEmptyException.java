package ru.rakhcheev.tasket.api.tasketapi.exception;

public class DatabaseIsEmptyException extends Exception {
    public DatabaseIsEmptyException(String message) {
        super(message);
    }
}
