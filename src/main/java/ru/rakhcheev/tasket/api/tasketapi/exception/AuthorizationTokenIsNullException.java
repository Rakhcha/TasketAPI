package ru.rakhcheev.tasket.api.tasketapi.exception;

public class AuthorizationTokenIsNullException extends Exception {
    public AuthorizationTokenIsNullException(String message) {
        super(message);
    }
}
