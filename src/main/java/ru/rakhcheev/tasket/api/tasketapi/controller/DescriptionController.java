package ru.rakhcheev.tasket.api.tasketapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.rakhcheev.tasket.api.tasketapi.entity.DescriptionEntity;
import ru.rakhcheev.tasket.api.tasketapi.exception.AuthorizationTokenIsNullException;
import ru.rakhcheev.tasket.api.tasketapi.exception.DescriptionIsNotFoundException;
import ru.rakhcheev.tasket.api.tasketapi.exception.DescriptionTableIsEmptyException;
import ru.rakhcheev.tasket.api.tasketapi.services.DescriptionService;

@Controller
@RequestMapping("/description")
public class DescriptionController {

    @Autowired
    private DescriptionService descriptionService;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getDescriptionById(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(descriptionService.getByUserId(id));
        } catch (DescriptionIsNotFoundException | DescriptionTableIsEmptyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Произошла ошибка при запросе");
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity getDescriptionById(@RequestHeader(value = "Authorization", defaultValue = "-1") String token,
                                             @RequestBody DescriptionEntity newDescriptionEntity){
        try{
            if(token.equals("-1")) throw new AuthorizationTokenIsNullException("Нет доступа");
            descriptionService.updateDescription(token,newDescriptionEntity);
            return ResponseEntity.ok().body(token);
        } catch (DescriptionIsNotFoundException | DescriptionTableIsEmptyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Произошла ошибка при запросе");
        }
    }

}