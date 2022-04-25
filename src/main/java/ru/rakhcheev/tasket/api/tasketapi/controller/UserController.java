package ru.rakhcheev.tasket.api.tasketapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rakhcheev.tasket.api.tasketapi.entity.UserEntity;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserAlreadyExistException;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserDatabaseIsEmptyException;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserNotFoundException;
import ru.rakhcheev.tasket.api.tasketapi.services.UserService;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addUser(@RequestBody UserEntity user){
        try {
            userService.addUser(user);
            return ResponseEntity.ok("Пользователь добавлен");
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Ошибка при добавлении пользователя\n" +
                    e.getCause().getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getUsers(@RequestParam(value = "showDescription", defaultValue = "false") Boolean showDescription){
        try {
            return ResponseEntity.ok().body(userService.getAllUsers(showDescription));
        } catch (UserDatabaseIsEmptyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getUser(@PathVariable Long id,
                                  @RequestParam(value = "showDescription",defaultValue = "false") Boolean showDescription){
        try {
            return ResponseEntity.ok().body(userService.getUserById(id, showDescription));
        } catch (UserDatabaseIsEmptyException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity editUser(@PathVariable Long id,
                                   @RequestBody UserEntity newUserParams){
        try {
            userService.editUser(id,newUserParams);
            return ResponseEntity.ok("Операция выполнена.");
        } catch (UserDatabaseIsEmptyException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Пользователь удалён.");
        } catch (UserDatabaseIsEmptyException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }


}