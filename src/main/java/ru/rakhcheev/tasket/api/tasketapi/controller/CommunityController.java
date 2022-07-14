package ru.rakhcheev.tasket.api.tasketapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rakhcheev.tasket.api.tasketapi.dto.community.CommunityCreationDTO;
import ru.rakhcheev.tasket.api.tasketapi.exception.CommunityAlreadyExistException;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserHasNotPermissionToCreateMoreThanOneCommunityException;
import ru.rakhcheev.tasket.api.tasketapi.services.CommunityService;

@RestController
@RequestMapping(value = "/community")
public class CommunityController {

    private final CommunityService communityService;

    @Autowired
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping
    public ResponseEntity<String> addCommunityRequest(@RequestBody CommunityCreationDTO community,
                                                      Authentication authentication){
        String userLogin = authentication.getName();
        try {
            communityService.addCommunity(userLogin, community);
            return new ResponseEntity<>("Группа" + community.getCommunityName() + " добавлена.", HttpStatus.OK);
        } catch (UserHasNotPermissionToCreateMoreThanOneCommunityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (CommunityAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get all communities

    // Get community by ID

    // Delete community by ID

    // Update community by ID

}
