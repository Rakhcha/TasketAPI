package ru.rakhcheev.tasket.api.tasketapi.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rakhcheev.tasket.api.tasketapi.dto.user.UserCreationDTO;
import ru.rakhcheev.tasket.api.tasketapi.dto.user.UserDTO;
import ru.rakhcheev.tasket.api.tasketapi.entity.DescriptionEntity;
import ru.rakhcheev.tasket.api.tasketapi.entity.UserEntity;
import ru.rakhcheev.tasket.api.tasketapi.entity.enums.UserAuthorityEnum;
import ru.rakhcheev.tasket.api.tasketapi.exception.NotFoundException;
import ru.rakhcheev.tasket.api.tasketapi.exception.DatabaseIsEmptyException;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserAlreadyExistException;
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

    public void addUser(UserCreationDTO userDTO) throws UserAlreadyExistException {
        if (userRepo.findByLogin(userDTO.getLogin()) != null)
            throw new UserAlreadyExistException("Пользователь под логином " + userDTO.getLogin() + " уже существует.");

        DescriptionEntity descriptionEntity = new DescriptionEntity();
        UserEntity user = UserCreationDTO.toEntity(userDTO);
        user.setAuthority(UserAuthorityEnum.STARTED);
        user.setDescription(descriptionEntity);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public List<UserDTO> getAllUsers(boolean showDescription) throws DatabaseIsEmptyException {
        if (!userRepo.findAll().iterator().hasNext())
            throw new DatabaseIsEmptyException("База данных пользователей пуста.");
        List<UserDTO> userList = new ArrayList<>();
        for (UserEntity user : userRepo.findAll()) {
            if (!showDescription) user.setDescription(null);
            userList.add(UserDTO.toDTO(user));
        }
        return userList;
    }

    public UserDTO getUserById(Long id, boolean showDescription) throws DatabaseIsEmptyException, NotFoundException {
        UserEntity user = getUserFromDatabase(id);
        if (!showDescription) user.setDescription(null);
        return UserDTO.toDTO(user);
    }

    public String editUserByLogin(String userLogin, UserCreationDTO newUserParams)
            throws NotFoundException, DatabaseIsEmptyException {
        UserEntity user = getUserFromDatabase(userLogin);
        return editUser(user, newUserParams);
    }

    public String editUserById(Long id, UserCreationDTO newUserParams)
            throws DatabaseIsEmptyException, NotFoundException, IllegalArgumentException {
        UserEntity user = getUserFromDatabase(id);
        return editUser(user, newUserParams);
    }

    public String deleteUser(Long id) throws DatabaseIsEmptyException, NotFoundException {
        UserEntity user = getUserFromDatabase(id);
        userRepo.delete(user);
        return user.getLogin();
    }

    private String editUser(UserEntity user, UserCreationDTO newUserParams) {
        if (newUserParams.getLogin() != null) user.setLogin(newUserParams.getLogin());
        if (newUserParams.getEmail() != null) user.setEmail(newUserParams.getEmail());
        if (newUserParams.getPassword() != null)
            user.setPassword(bCryptPasswordEncoder.encode(newUserParams.getPassword()));
        if (newUserParams.getRole() != null) {
            UserAuthorityEnum role = UserAuthorityEnum.valueOf(newUserParams.getRole().toUpperCase());
            user.setAuthority(role);
        }
        userRepo.save(user);
        return user.getLogin();
    }

    private UserEntity getUserFromDatabase(Long id) throws DatabaseIsEmptyException, NotFoundException {
        if (!userRepo.findAll().iterator().hasNext())
            throw new DatabaseIsEmptyException("База данных пользователей пуста");
        Optional<UserEntity> userEntityOptional = userRepo.findById(id);
        if (userEntityOptional.isEmpty())
            throw new NotFoundException("Пользователь с идентификатором" + id + " не найден");
        return userEntityOptional.get();
    }

    private UserEntity getUserFromDatabase(String login) throws DatabaseIsEmptyException, NotFoundException {
        if (!userRepo.findAll().iterator().hasNext())
            throw new DatabaseIsEmptyException("База данных пользователей пуста");
        UserEntity userEntity = userRepo.findByLogin(login);
        if (userEntity == null) throw new NotFoundException("Пользователь " + login + " не найден");
        return userEntity;
    }

}
