package ru.rakhcheev.tasket.api.tasketapi.dto.community;

import lombok.Data;

@Data
public class CommunityCreateUrlDTO {
    private Long communityId;
    private String destroyDate;
    private Boolean onceUsed;
}
