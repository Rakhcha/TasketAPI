package ru.rakhcheev.tasket.api.tasketapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rakhcheev.tasket.api.tasketapi.dto.community.*;
import ru.rakhcheev.tasket.api.tasketapi.exception.*;
import ru.rakhcheev.tasket.api.tasketapi.services.CommunityService;

import java.time.format.DateTimeParseException;
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
                                                      Authentication authentication) {
        String userLogin = authentication.getName();
        try {
            communityService.addCommunity(userLogin, community);
            return new ResponseEntity<>("Группа" + community.getCommunityName() + " добавлена.", HttpStatus.OK);
        } catch (UserHasNotPermission e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (CommunityAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getCommunities(@RequestParam(value = "type", defaultValue = "public") String type,
                                            Authentication authentication) {
        try {
            List<CommunityInfoDTO> communityList = communityService.getCommunities(type, authentication.getName());
            return new ResponseEntity<>(communityList, HttpStatus.OK);
        } catch (CommunityEnumTypeIsNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (TableIsEmptyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getCommunityById(@PathVariable(value = "id") Long id,
                                              Authentication authentication) {
        try {
            CommunityDTO communityList = communityService.getCommunityById(id, authentication.getName());
            return new ResponseEntity<>(communityList, HttpStatus.OK);
        } catch (NotFoundException | UserHasNotPermission e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // TODO When delete community all users and invite urls was deleted

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteCommunityById(@PathVariable(value = "id") Long id, Authentication authentication) {
        try {
            communityService.deleteCommunityById(id, authentication.getName());
            return new ResponseEntity<>("Группа успешно удалена", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UserHasNotPermission e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/url")
    public ResponseEntity<?> addUrlForJoinCommunityById(@RequestBody CommunityCreateUrlDTO communityCreateUrlDTO,
                                                        Authentication authentication) {
        try {
            CommunityUrlDTO communityUrlDTO = communityService.addInviteUrl(
                    communityCreateUrlDTO,
                    authentication.getName()
            );
            return new ResponseEntity<>(communityUrlDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UserHasNotPermission e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (CommunityHasTooManyUrlsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Неверный формат времени (пример: 2018-05-05T11:50:55.1234)", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Update community by ID

    // POST Join into Community by Authentication if Community is not Private

    @PostMapping(value = "/join/{inviteKey}")
    public ResponseEntity<String> joinCommunityWithInviteKey(@PathVariable(value = "inviteKey") String inviteKey, Authentication authentication) {
        try {
            communityService.joinWithInviteKey(inviteKey, authentication);
            return new ResponseEntity<>("Пользователь добавлен в группу", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла непредвиденная ошибка: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
