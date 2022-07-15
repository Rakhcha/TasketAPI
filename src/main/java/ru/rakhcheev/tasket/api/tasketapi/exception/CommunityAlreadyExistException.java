package ru.rakhcheev.tasket.api.tasketapi.exception;

public class CommunityAlreadyExistException extends Exception {

    public CommunityAlreadyExistException(String communityName) {
        super("Группа " + communityName + " уже сущетсвует.");
    }
}
