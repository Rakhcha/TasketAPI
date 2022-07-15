package ru.rakhcheev.tasket.api.tasketapi.exception;

public class UserHasNotPermission extends Exception {

    public UserHasNotPermission(String message) {
        super(message);
    }
}
