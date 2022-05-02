package ru.rakhcheev.tasket.api.tasketapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.rakhcheev.tasket.api.tasketapi.entity.UserEntity;

public class User {

    private Long id;
    private String login;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Description description;

    public User() {
    }

    public static User toModel(UserEntity userEntity) {
        User model = new User();
        model.setId(userEntity.getId());
        model.setLogin(userEntity.getLogin());
        model.setEmail(userEntity.getEmail());
        model.setDescription(Description.toModel(userEntity.getDescription()));
        return model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }
}
