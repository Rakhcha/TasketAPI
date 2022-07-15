package ru.rakhcheev.tasket.api.tasketapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rakhcheev.tasket.api.tasketapi.dto.community.CommunityCreationDTO;
import ru.rakhcheev.tasket.api.tasketapi.dto.community.CommunityDTO;
import ru.rakhcheev.tasket.api.tasketapi.dto.community.CommunityInfoDTO;
import ru.rakhcheev.tasket.api.tasketapi.entity.CommunityEntity;
import ru.rakhcheev.tasket.api.tasketapi.entity.CommunitySearchTypeEnum;
import ru.rakhcheev.tasket.api.tasketapi.entity.EntityStatusEnum;
import ru.rakhcheev.tasket.api.tasketapi.entity.UserEntity;
import ru.rakhcheev.tasket.api.tasketapi.exception.*;
import ru.rakhcheev.tasket.api.tasketapi.repository.CommunityRepo;
import ru.rakhcheev.tasket.api.tasketapi.repository.UserRepo;

import java.util.*;

@Service
public class CommunityService {

    private final UserRepo userRepo;
    private final CommunityRepo communityRepo;

    @Value("${community.count.max}")
    private int COUNT_OF_MAX_COMMUNITIES;

    @Autowired
    public CommunityService(UserRepo userRepo, CommunityRepo communityRepo) {
        this.userRepo = userRepo;
        this.communityRepo = communityRepo;
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

        return CommunityDTO.toDTO(community);
    }

    public void deleteCommunityById(Long id, String userLogin) throws NotFoundException, UserHasNotPermission {
        Optional<CommunityEntity> communityEntityOptional = communityRepo.findById(id);
        UserEntity user;
        CommunityEntity community;

        if(communityEntityOptional.isEmpty()) throw new NotFoundException("Группы с id: " + id + " не существует.");
        community = communityEntityOptional.get();

        if(community.getStatusActivity().ordinal() == 3) throw new NotFoundException("Группы с id: " + id + " не существует.");
        user = userRepo.findByLogin(userLogin);

        // TODO if(проверка на роль администратора) communityRepo.delete(community);
        if(!community.getCreator().equals(user)) throw new UserHasNotPermission("Только создатель может удалить группу");
        community.setStatusActivity(EntityStatusEnum.DELETED);
        communityRepo.save(community);
    }

    private boolean canCreateCommunity(UserEntity user) {

        int countOfCreatedGroups = user.getSetOfCreatedGroups().size();
        boolean canCreateMoreThanOneCommunity = false;

        if (countOfCreatedGroups == 0) return true;
        // TODO проверка на права создания более одного и комьюнити

        return canCreateMoreThanOneCommunity && countOfCreatedGroups < COUNT_OF_MAX_COMMUNITIES;
    }
}
