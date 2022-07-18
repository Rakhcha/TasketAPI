package ru.rakhcheev.tasket.api.tasketapi.exception;

public class CommunityHasTooManyUrlsException extends Exception{
    public CommunityHasTooManyUrlsException() {
        super("Данное сообщество имеет слишком много адресов");
    }
}
