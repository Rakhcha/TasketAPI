package ru.rakhcheev.tasket.api.tasketapi.exception;

public class TableIsEmptyException extends Exception {
    public TableIsEmptyException(String message) {
        super(message);
    }
}
