package ru.rakhcheev.tasket.api.tasketapi.dto.community;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.rakhcheev.tasket.api.tasketapi.entity.InviteUrlEntity;

import java.time.LocalDateTime;

@Data
public class CommunityUrlDTO {

    private String communityName;
    private String urlParam;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime destroyDate;

    public static CommunityUrlDTO toDTO(InviteUrlEntity communityUrlEntity){
        CommunityUrlDTO dto = new CommunityUrlDTO();
        dto.setCommunityName(communityUrlEntity.getCommunity().getCommunityName());
        dto.setUrlParam(communityUrlEntity.getUrlParam());
        dto.setDestroyDate(communityUrlEntity.getDestroyDate());
        return dto;
    }

}
