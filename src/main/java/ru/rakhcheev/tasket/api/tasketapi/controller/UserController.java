package ru.rakhcheev.tasket.api.tasketapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rakhcheev.tasket.api.tasketapi.dto.user.UserCreationDTO;
import ru.rakhcheev.tasket.api.tasketapi.dto.user.UserDTO;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserAlreadyExistException;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserDatabaseIsEmptyException;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserNotFoundException;
import ru.rakhcheev.tasket.api.tasketapi.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UserCreationDTO user) {
        try {
            userService.addUser(user);
            return new ResponseEntity<>("Пользователь " + user.getLogin() + " добавлен.", HttpStatus.OK);
        } catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(value = "showDescription", defaultValue = "false") Boolean showDescription) {
        try {
            List<UserDTO> users = userService.getAllUsers(showDescription);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (UserDatabaseIsEmptyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id,
                                     @RequestParam(value = "showDescription", defaultValue = "false") Boolean showDescription) {
        try {
            UserDTO user = userService.getUserById(id, showDescription);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserDatabaseIsEmptyException | UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> editUser(@PathVariable Long id,
                                           @RequestBody UserCreationDTO newUserParams) {
        try {
            String login = userService.editUser(id, newUserParams);
            return new ResponseEntity<>("Данные пользователя " + login + " изменены.", HttpStatus.OK);
        } catch (UserDatabaseIsEmptyException | UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Попытка ввода несуществующей роли", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    // TODO add edit user by Authentication

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            String login = userService.deleteUser(id);
            return ResponseEntity.ok("Пользователь " + login + " удалён.");
        } catch (UserDatabaseIsEmptyException | UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }


}