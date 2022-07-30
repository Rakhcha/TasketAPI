package ru.rakhcheev.tasket.api.tasketapi.dto.category;

import lombok.Data;
import ru.rakhcheev.tasket.api.tasketapi.entity.CategoryEntity;

@Data
public class CategoryDTO {

    private Long id;
    private String title;
    private String leaderLogin;

    public CategoryDTO(Long id, String title, String leaderLogin) {
        this.id = id;
        this.title = title;
        this.leaderLogin = leaderLogin;
    }

    public static CategoryDTO toDTO(CategoryEntity entity) {
        return new CategoryDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getLeader().getLogin()
        );
    }

}
