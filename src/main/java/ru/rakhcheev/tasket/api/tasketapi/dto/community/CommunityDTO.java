package ru.rakhcheev.tasket.api.tasketapi.dto.community;

import lombok.Data;
import ru.rakhcheev.tasket.api.tasketapi.dto.user.UserDTO;
import ru.rakhcheev.tasket.api.tasketapi.entity.CommunityEntity;
import ru.rakhcheev.tasket.api.tasketapi.entity.UserEntity;

import java.util.HashSet;
import java.util.Set;

@Data
public class CommunityDTO {

    private String communityName;
    private Boolean isPrivate;
    private UserDTO creator;
    private Set<UserDTO> users;

    public static CommunityDTO toDTO(CommunityEntity communityEntity) {
        CommunityDTO communityDTO = new CommunityDTO();
        communityDTO.setCommunityName(communityEntity.getCommunityName());
        communityDTO.setIsPrivate(communityEntity.getIsPrivate());

        UserEntity user = communityEntity.getCreator();
        user.setDescription(null);
        communityDTO.setCreator(UserDTO.toDTO(user));

        Set<UserDTO> users = new HashSet<>();
        for (UserEntity userEntity : communityEntity.getUsersSet()) {
            userEntity.setDescription(null);
            users.add(UserDTO.toDTO(userEntity));
        }
        communityDTO.setUsers(users);
        return communityDTO;
    }

}
