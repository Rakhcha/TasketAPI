package ru.rakhcheev.tasket.api.tasketapi.exception;

public class UserHasNotPermissionToCreateMoreThanOneCommunityException extends Exception{

    public UserHasNotPermissionToCreateMoreThanOneCommunityException() {
        super("Нет прав для создания более одной группы");
    }
}
