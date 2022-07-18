package ru.rakhcheev.tasket.api.tasketapi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rakhcheev.tasket.api.tasketapi.entity.CommunityEntity;
import ru.rakhcheev.tasket.api.tasketapi.entity.CommunityUrlEntity;

import java.util.List;

public interface CommunityUrlRepo extends CrudRepository<CommunityUrlEntity, Long> {
    CommunityUrlEntity findByUrlParam(String urlParam);
    List<CommunityUrlEntity> findAllByCommunity(CommunityEntity community);
}
