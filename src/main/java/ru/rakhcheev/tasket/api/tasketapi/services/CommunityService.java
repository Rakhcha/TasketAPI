package ru.rakhcheev.tasket.api.tasketapi.services;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rakhcheev.tasket.api.tasketapi.dto.community.CommunityCreationDTO;
import ru.rakhcheev.tasket.api.tasketapi.entity.CommunityEntity;
import ru.rakhcheev.tasket.api.tasketapi.entity.UserEntity;
import ru.rakhcheev.tasket.api.tasketapi.exception.CommunityAlreadyExistException;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserHasNotPermissionToCreateMoreThanOneCommunityException;
import ru.rakhcheev.tasket.api.tasketapi.repository.CommunityRepo;
import ru.rakhcheev.tasket.api.tasketapi.repository.DescriptionRepo;
import ru.rakhcheev.tasket.api.tasketapi.repository.UserRepo;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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

    public void addCommunity(String creatorLogin, CommunityCreationDTO community) throws UserHasNotPermissionToCreateMoreThanOneCommunityException, CommunityAlreadyExistException {

        UserEntity user = userRepo.findByLogin(creatorLogin);

        if(!canCreateCommunity(user)) throw new UserHasNotPermissionToCreateMoreThanOneCommunityException();
        if(communityRepo.findByCommunityName(community.getCommunityName()) != null)
            throw new CommunityAlreadyExistException(community.getCommunityName());

        CommunityEntity communityEntity = CommunityCreationDTO.toEntity(community);
        communityEntity.setCreator(user);

        Set<UserEntity> userEntitySet = new HashSet<>();
        userEntitySet.add(user);
        communityEntity.setUsersSet(userEntitySet);

        communityRepo.save(communityEntity);
    }

    private boolean canCreateCommunity(UserEntity user){

        int countOfCreatedGroups = user.getSetOfCreatedGroups().size();
        boolean canCreateMoreThanOneCommunity = false;

        if(countOfCreatedGroups == 0) return true;
        // TODO проверка на права создания более одного и комьюнити

        return canCreateMoreThanOneCommunity && countOfCreatedGroups < 10;
    }
}
