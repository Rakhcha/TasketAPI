package ru.rakhcheev.tasket.api.tasketapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.rakhcheev.tasket.api.tasketapi.entity.DescriptionEntity;
import ru.rakhcheev.tasket.api.tasketapi.exception.DescriptionIsNotFoundException;
import ru.rakhcheev.tasket.api.tasketapi.exception.DescriptionTableIsEmptyException;
import ru.rakhcheev.tasket.api.tasketapi.services.DescriptionService;
import ru.rakhcheev.tasket.api.tasketapi.services.UserService;

@Controller
@RequestMapping("/description")
public class DescriptionController {

    private final DescriptionService descriptionService;

    public DescriptionController(DescriptionService descriptionService, UserService userService) {
        this.descriptionService = descriptionService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getDescriptionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(descriptionService.getByUserId(id));
        } catch (DescriptionIsNotFoundException | DescriptionTableIsEmptyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка при запросе");
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity getDescriptionById(Authentication authentication,
                                             @RequestBody DescriptionEntity newDescriptionEntity ) {
        try {
            descriptionService.updateDescription(authentication.getName(), newDescriptionEntity);
            return ResponseEntity.ok().body("Данные обновлены ");
        } catch (DescriptionIsNotFoundException | DescriptionTableIsEmptyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка при запросе");
        }
    }

}