package ru.rakhcheev.tasket.api.tasketapi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rakhcheev.tasket.api.tasketapi.entity.UserEntity;

public interface UserRepo extends CrudRepository<UserEntity, Long> {

    UserEntity findByLogin(String login);

}
