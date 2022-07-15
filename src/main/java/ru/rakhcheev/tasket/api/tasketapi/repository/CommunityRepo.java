package ru.rakhcheev.tasket.api.tasketapi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rakhcheev.tasket.api.tasketapi.entity.CommunityEntity;

public interface CommunityRepo extends CrudRepository<CommunityEntity, Long> {

    CommunityEntity findByCommunityName(String communityName);

}