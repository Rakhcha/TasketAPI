package ru.rakhcheev.tasket.api.tasketapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rakhcheev.tasket.api.tasketapi.dto.community.CommunityCreationDTO;
import ru.rakhcheev.tasket.api.tasketapi.dto.community.CommunityDTO;
import ru.rakhcheev.tasket.api.tasketapi.dto.community.CommunityInfoDTO;
import ru.rakhcheev.tasket.api.tasketapi.exception.*;
import ru.rakhcheev.tasket.api.tasketapi.services.CommunityService;

import java.util.List;

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
        } catch (UserHasNotPermission e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (CommunityAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getCommunities(@RequestParam(value = "type", defaultValue = "public") String type,
                                                     Authentication authentication) {
        try {
            List<CommunityInfoDTO> communityList = communityService.getCommunities(type, authentication.getName());
            return new ResponseEntity<>(communityList, HttpStatus.OK) ;
        } catch (CommunityEnumTypeIsNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (TableIsEmptyException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getCommunityById(@PathVariable(value = "id") Long id,
                                            Authentication authentication) {
        try {
            CommunityDTO communityList = communityService.getCommunityById(id, authentication.getName());
            return new ResponseEntity<>(communityList, HttpStatus.OK) ;
        } catch (CommunityNotFoundException | UserHasNotPermission e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get community by Name

    // Delete community by ID

    // Update community by ID

}
