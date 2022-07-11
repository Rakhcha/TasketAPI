package ru.rakhcheev.tasket.api.tasketapi.dto.user;

import lombok.Data;
import ru.rakhcheev.tasket.api.tasketapi.entity.UserEntity;

@Data
public class UserCreationDTO {

    private String login;
    private String password;
    private String email;

    private UserCreationDTO() {
    }

    public static UserEntity toEntity(UserCreationDTO userCreationDTO) {
        UserEntity user = new UserEntity();
        user.setLogin(userCreationDTO.getLogin());
        user.setPassword(userCreationDTO.getPassword());
        user.setEmail(userCreationDTO.getEmail());
        return user;
    }
}
