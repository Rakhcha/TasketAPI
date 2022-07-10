package ru.rakhcheev.tasket.api.tasketapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.rakhcheev.tasket.api.tasketapi.entity.UserEntity;

@Data
public class User {

    private Long id;
    private String login;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Description description;

    private User() {
    }

    public static User toModel(UserEntity userEntity) {
        User model = new User();
        model.setId(userEntity.getId());
        model.setLogin(userEntity.getLogin());
        model.setEmail(userEntity.getEmail());
        model.setDescription(Description.toModel(userEntity.getDescription()));
        return model;
    }
}
