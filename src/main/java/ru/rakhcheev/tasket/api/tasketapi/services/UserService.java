package ru.rakhcheev.tasket.api.tasketapi.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rakhcheev.tasket.api.tasketapi.entity.DescriptionEntity;
import ru.rakhcheev.tasket.api.tasketapi.entity.UserEntity;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserAlreadyExistException;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserDatabaseIsEmptyException;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserNotFoundException;
import ru.rakhcheev.tasket.api.tasketapi.model.User;
import ru.rakhcheev.tasket.api.tasketapi.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserEntity addUser(UserEntity user) throws UserAlreadyExistException {
        if (userRepo.findByLogin(user.getLogin()) != null) throw  new UserAlreadyExistException("Пользователь уже сущесвует");
        DescriptionEntity descriptionEntity = new DescriptionEntity();
        user.setDescription(descriptionEntity);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public List<User> getAllUsers(boolean showDescription) throws UserDatabaseIsEmptyException {
        if (!userRepo.findAll().iterator().hasNext()) throw new UserDatabaseIsEmptyException("База данных пользователей пуста.");
        List<User> userList = new ArrayList<>();
        for(UserEntity user : userRepo.findAll()) {
            if(!showDescription) user.setDescription(null);
            userList.add(User.toModel(user));
        }
        return userList;
    }

    public User getUserById(Long id, boolean showDescription) throws UserDatabaseIsEmptyException, UserNotFoundException{
        UserEntity user = getUserFromDatabase(id);
        if(!showDescription) user.setDescription(null);
        return User.toModel(user);
    }

    public void editUser(Long id,UserEntity newUserParams) throws UserDatabaseIsEmptyException, UserNotFoundException {
        UserEntity user = getUserFromDatabase(id);
        if(newUserParams.getLogin() != null) user.setLogin(newUserParams.getLogin());
        if(newUserParams.getEmail() != null) user.setEmail(newUserParams.getEmail());
        if(newUserParams.getPassword() != null) user.setPassword(bCryptPasswordEncoder.encode(newUserParams.getPassword()));
        userRepo.save(user);
    }

    public void deleteUser(Long id) throws UserDatabaseIsEmptyException, UserNotFoundException {
        UserEntity user = getUserFromDatabase(id);
        userRepo.delete(user);
    }

    private UserEntity getUserFromDatabase(Long id) throws UserDatabaseIsEmptyException, UserNotFoundException {
        if (!userRepo.findAll().iterator().hasNext()) throw new UserDatabaseIsEmptyException("База данных пользователей пуста");
        Optional<UserEntity> userEntityOptional = userRepo.findById(id);
        if(userEntityOptional.isEmpty()) throw new UserNotFoundException("Пользователь не найден");
        return userEntityOptional.get();
    }

}
