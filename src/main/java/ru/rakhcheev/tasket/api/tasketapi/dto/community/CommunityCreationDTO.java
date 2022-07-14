package ru.rakhcheev.tasket.api.tasketapi.dto.community;

import lombok.Data;
import ru.rakhcheev.tasket.api.tasketapi.entity.CommunityEntity;

@Data
public class CommunityCreationDTO {

    private String communityName;
    private Boolean isPrivate;

    public static CommunityEntity toEntity(CommunityCreationDTO communityCreationDTO){
        CommunityEntity entity = new CommunityEntity();
        entity.setCommunityName(communityCreationDTO.getCommunityName());
        entity.setIsPrivate(communityCreationDTO.getIsPrivate());
        return entity;
    }
}
