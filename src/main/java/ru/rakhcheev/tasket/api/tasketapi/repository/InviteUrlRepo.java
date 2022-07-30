package ru.rakhcheev.tasket.api.tasketapi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rakhcheev.tasket.api.tasketapi.entity.CommunityEntity;
import ru.rakhcheev.tasket.api.tasketapi.entity.InviteUrlEntity;

import java.util.List;

public interface InviteUrlRepo extends CrudRepository<InviteUrlEntity, Long> {
    InviteUrlEntity findByUrlParam(String urlParam);
    List<InviteUrlEntity> findAllByCommunity(CommunityEntity community);
}
