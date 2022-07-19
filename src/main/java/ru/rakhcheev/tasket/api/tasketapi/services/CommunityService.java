package ru.rakhcheev.tasket.api.tasketapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.rakhcheev.tasket.api.tasketapi.dto.community.*;
import ru.rakhcheev.tasket.api.tasketapi.entity.*;
import ru.rakhcheev.tasket.api.tasketapi.exception.*;
import ru.rakhcheev.tasket.api.tasketapi.repository.CommunityRepo;
import ru.rakhcheev.tasket.api.tasketapi.repository.CommunityUrlRepo;
import ru.rakhcheev.tasket.api.tasketapi.repository.UserRepo;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CommunityService {

    private final UserRepo userRepo;
    private final CommunityRepo communityRepo;
    private final CommunityUrlRepo communityUrlRepo;

    @Value("${community.count.max}")
    private int COUNT_OF_MAX_COMMUNITIES;

    @Autowired
    public CommunityService(UserRepo userRepo, CommunityRepo communityRepo, CommunityUrlRepo communityUrlRepo) {
        this.userRepo = userRepo;
        this.communityRepo = communityRepo;
        this.communityUrlRepo = communityUrlRepo;
    }

    public void addCommunity(String creatorLogin, CommunityCreationDTO community) throws UserHasNotPermission, CommunityAlreadyExistException {

        UserEntity user = userRepo.findByLogin(creatorLogin);

        if (!canCreateCommunity(user)) throw new UserHasNotPermission("Нет прав для создания более одной группы");
        if (communityRepo.findByCommunityName(community.getCommunityName()) != null)
            throw new CommunityAlreadyExistException(community.getCommunityName());

        CommunityEntity communityEntity = CommunityCreationDTO.toEntity(community);
        communityEntity.setCreator(user);

        Set<UserEntity> userEntitySet = new HashSet<>();
        userEntitySet.add(user);
        communityEntity.setUsersSet(userEntitySet);

        communityRepo.save(communityEntity);
    }


    public List<CommunityInfoDTO> getCommunities(String type, String userLogin) throws CommunityEnumTypeIsNotFoundException, TableIsEmptyException {
        CommunitySearchTypeEnum communityTypeEnum;
        Iterable<CommunityEntity> communitiesIterableList;
        List<CommunityInfoDTO> communitiesList;

        try {
            communityTypeEnum = Enum.valueOf(CommunitySearchTypeEnum.class, type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommunityEnumTypeIsNotFoundException("Аргумент \"type\": " + type + " не поддерживается.");
        }

        communitiesList = new ArrayList<>();
        communitiesIterableList = communityRepo.findAll();

        if (!communitiesIterableList.iterator().hasNext())
            throw new TableIsEmptyException("Ни одной группы не создано");

        switch (communityTypeEnum) {
            case JOINED:
                UserEntity user = userRepo.findByLogin(userLogin);
                for (CommunityEntity community : communitiesIterableList)
                    if (community.getUsersSet().contains(user)) communitiesList.add(CommunityInfoDTO.toDTO(community));
                return communitiesList;
            case PUBLIC:
                for (CommunityEntity community : communitiesIterableList)
                    if (!community.getIsPrivate()) communitiesList.add(CommunityInfoDTO.toDTO(community));
                return communitiesList;
            case CREATED:
                for (CommunityEntity community : communitiesIterableList)
                    if (community.getCreator().getLogin().equals(userLogin))
                        communitiesList.add(CommunityInfoDTO.toDTO(community));
                return communitiesList;
            default:
                for (CommunityEntity community : communitiesIterableList)
                    communitiesList.add(CommunityInfoDTO.toDTO(community));
                return communitiesList;
        }
    }

    public CommunityDTO getCommunityById(Long id, String userLogin) throws NotFoundException, UserHasNotPermission {
        CommunityEntity community = getCommunityEntity(id, userLogin);
        return CommunityDTO.toDTO(community);
    }

    public void deleteCommunityById(Long id, String userLogin) throws NotFoundException, UserHasNotPermission {
        Optional<CommunityEntity> communityEntityOptional = communityRepo.findById(id);
        UserEntity user;
        CommunityEntity community;

        if (communityEntityOptional.isEmpty()) throw new NotFoundException("Группы с id: " + id + " не существует.");
        community = communityEntityOptional.get();

        if (community.getStatusActivity().ordinal() == 3)
            throw new NotFoundException("Группы с id: " + id + " не существует.");
        user = userRepo.findByLogin(userLogin);

        // TODO if(проверка на роль администратора) communityRepo.delete(community);
        if (!community.getCreator().equals(user))
            throw new UserHasNotPermission("Только создатель может удалить группу");
        community.setStatusActivity(EntityStatusEnum.DELETED);
        communityRepo.save(community);
    }

    public CommunityUrlDTO addInviteUrl(CommunityCreateUrlDTO communityCreateUrlDTO, String userLogin)
            throws CommunityHasTooManyUrlsException, NotFoundException, UserHasNotPermission {

        CommunityUrlEntity url;
        CommunityEntity community;
        String dateTimeString;
        Long communityId = communityCreateUrlDTO.getCommunityId();
        Optional<CommunityEntity> communityEntityOptional = communityRepo.findById(communityId);

        if (communityEntityOptional.isEmpty() || communityEntityOptional.get().getStatusActivity().equals(EntityStatusEnum.DELETED))
            throw new NotFoundException("Группа с id: " + communityId + " не найдена.");
        community = communityEntityOptional.get();

        if (communityUrlRepo.findAllByCommunity(community).size() >= 5) throw new CommunityHasTooManyUrlsException();

        url = new CommunityUrlEntity(community);
        while (communityUrlRepo.findByUrlParam(url.getUrlParam()) != null) url.regenerateUrlParam();

        dateTimeString = communityCreateUrlDTO.getDestroyDate();
        url.setDestroyDate(
                dateTimeString == null || dateTimeString.isBlank()
                        ? null
                        : LocalDateTime.parse(dateTimeString)
        );
        if(communityCreateUrlDTO.getOnceUsed() != null) url.setOnceUsed(communityCreateUrlDTO.getOnceUsed());
        communityUrlRepo.save(url);
        return CommunityUrlDTO.toDTO(url);
    }

    public void joinWithInviteKey(String inviteKey, Authentication authentication)
            throws NotFoundException, UserAlreadyExistException {
        UserEntity user;
        CommunityUrlEntity urlEntity = communityUrlRepo.findByUrlParam(inviteKey);
        CommunityEntity community;

        if (urlEntity.getStatus().equals(EntityStatusEnum.DELETED))
            throw new NotFoundException("Данного ключа не существует, либо он был использован");
        if (urlEntity.getDestroyDate() != null && urlEntity.getDestroyDate().isBefore(LocalDateTime.now())) {
            urlEntity.setStatus(EntityStatusEnum.DELETED);
            throw new NotFoundException("Данного ключа не существует, либо он был использован");
        }

        user = userRepo.findByLogin(authentication.getName());
        community = urlEntity.getCommunity();

        if (community.getUsersSet().contains(user))
            throw new UserAlreadyExistException("Данный пользователь уже состоит в группе");

        community.getUsersSet().add(user);
        communityRepo.save(community);
        if (urlEntity.getOnceUsed()) {
            urlEntity.setStatus(EntityStatusEnum.DELETED);
            communityUrlRepo.save(urlEntity);
        }
    }

    public void joinPublicCommunity(Long id, Authentication authentication)
            throws NotFoundException, UserHasNotPermission, UserAlreadyExistException {
        UserEntity user;
        CommunityEntity community;

        community = getCommunityEntity(id, authentication.getName());
        user = userRepo.findByLogin(authentication.getName());
        if(community.getUsersSet().contains(user)) throw new UserAlreadyExistException("Данный пользователь уже состоит в группе");
        if(community.getIsPrivate()) throw new UserHasNotPermission("Нет прав для доступа, так как данная группа приватная");
        // TODO Проверка на удаленную группу.

        community.getUsersSet().add(user);
        communityRepo.save(community);
    }

    private CommunityEntity getCommunityEntity(Long id, String userLogin) throws NotFoundException, UserHasNotPermission {
        UserEntity user;
        CommunityEntity community;
        Optional<CommunityEntity> communityEntityOptional = communityRepo.findById(id);
        if (communityEntityOptional.isEmpty())
            throw new NotFoundException("Группа с id: " + id + " не найдена.");

        community = communityEntityOptional.get();
        user = userRepo.findByLogin(userLogin);

        // обратная импликация
        if (community.getIsPrivate() && !community.getUsersSet().contains(user))
            throw new UserHasNotPermission("Нет прав для доступа к группе");

        return community;
    }

    private boolean canCreateCommunity(UserEntity user) {

        int countOfCreatedGroups = user.getSetOfCreatedGroups().size();
        boolean canCreateMoreThanOneCommunity = false;

        if (countOfCreatedGroups == 0) return true;
        // TODO проверка на права создания более одного и комьюнити

        return canCreateMoreThanOneCommunity && countOfCreatedGroups < COUNT_OF_MAX_COMMUNITIES;
    }
}
