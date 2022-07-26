package ru.rakhcheev.tasket.api.tasketapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rakhcheev.tasket.api.tasketapi.dto.user.UserCreationDTO;
import ru.rakhcheev.tasket.api.tasketapi.dto.user.UserDTO;
import ru.rakhcheev.tasket.api.tasketapi.exception.AlreadyExistException;
import ru.rakhcheev.tasket.api.tasketapi.exception.NotFoundException;
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
        } catch (AlreadyExistException e) {
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
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> editUser(@PathVariable Long id,
                                           @RequestBody UserCreationDTO newUserParams) {
        try {
            String login = userService.editUserById(id, newUserParams);
            return new ResponseEntity<>("Данные пользователя " + login + " изменены.", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Попытка ввода несуществующей роли", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<String> editUserByAuthentication(Authentication authentication,
                                                           @RequestBody UserCreationDTO newUserParams) {
        try {
            String login = userService.editUserByLogin(authentication.getName(), newUserParams);
            return new ResponseEntity<>("Данные пользователя " + login + " изменены.", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Попытка ввода несуществующей роли", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            String login = userService.deleteUser(id);
            return ResponseEntity.ok("Пользователь " + login + " удалён.");
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }


}