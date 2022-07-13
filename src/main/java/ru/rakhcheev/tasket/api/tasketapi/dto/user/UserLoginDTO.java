package ru.rakhcheev.tasket.api.tasketapi.dto.user;

import lombok.Data;

@Data
public class UserLoginDTO {

    private String login;
    private String password;

}
