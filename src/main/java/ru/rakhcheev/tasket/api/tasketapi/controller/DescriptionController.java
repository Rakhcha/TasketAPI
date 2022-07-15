package ru.rakhcheev.tasket.api.tasketapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.rakhcheev.tasket.api.tasketapi.dto.description.DescriptionDTO;
import ru.rakhcheev.tasket.api.tasketapi.entity.DescriptionEntity;
import ru.rakhcheev.tasket.api.tasketapi.exception.DescriptionIsNotFoundException;
import ru.rakhcheev.tasket.api.tasketapi.exception.TableIsEmptyException;
import ru.rakhcheev.tasket.api.tasketapi.services.DescriptionService;

@Controller
@RequestMapping("/description")
public class DescriptionController {

    private final DescriptionService descriptionService;

    public DescriptionController(DescriptionService descriptionService) {
        this.descriptionService = descriptionService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<?> getDescriptionById(@PathVariable Long id) {
        try {
            DescriptionDTO descriptionDTO = DescriptionDTO.toDTO(descriptionService.getByUserId(id));
            return new ResponseEntity<>(descriptionDTO, HttpStatus.OK);
        } catch (DescriptionIsNotFoundException | TableIsEmptyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<String> updateDescriptionById(@PathVariable Long id,
                                                        @RequestBody DescriptionEntity newDescriptionEntity) {
        try {
            descriptionService.updateDescriptionById(id, newDescriptionEntity);
            return new ResponseEntity<>("Ваши данные обновлены", HttpStatus.OK);
        } catch (DescriptionIsNotFoundException | TableIsEmptyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<String> updateDescriptionByAuthentication(Authentication authentication,
                                                                    @RequestBody DescriptionEntity newDescriptionEntity) {
        try {
            descriptionService.updateDescriptionByLogin(authentication.getName(), newDescriptionEntity);
            return new ResponseEntity<>("Ваши данные обновлены", HttpStatus.OK);
        } catch (DescriptionIsNotFoundException | TableIsEmptyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

}