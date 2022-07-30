package ru.rakhcheev.tasket.api.tasketapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.rakhcheev.tasket.api.tasketapi.dto.category.CategoryDTO;
import ru.rakhcheev.tasket.api.tasketapi.entity.CategoryEntity;
import ru.rakhcheev.tasket.api.tasketapi.entity.CommunityEntity;
import ru.rakhcheev.tasket.api.tasketapi.entity.UserEntity;
import ru.rakhcheev.tasket.api.tasketapi.exception.NotFoundException;
import ru.rakhcheev.tasket.api.tasketapi.exception.UserHasNotPermission;
import ru.rakhcheev.tasket.api.tasketapi.repository.CategoryRepo;
import ru.rakhcheev.tasket.api.tasketapi.repository.CommunityRepo;
import ru.rakhcheev.tasket.api.tasketapi.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final CommunityRepo communityRepo;
    private final UserRepo userRepo;

    @Autowired
    public CategoryService(CategoryRepo categoryRepo, CommunityRepo communityRepo, UserRepo userRepo) {
        this.categoryRepo = categoryRepo;
        this.communityRepo = communityRepo;
        this.userRepo = userRepo;
    }

    // Получение всех категорий (админ) /category
    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();

        for (CategoryEntity entity : categoryRepo.findAll()) {
            CategoryDTO categoryDTO = CategoryDTO.toDTO(entity);
            categoryDTOS.add(categoryDTO);
        }

        return categoryDTOS;
    }

    // Получение категорий по id группы /category/community/id
    public List<CategoryDTO> getAllCategoriesByCommunityId(Long id, Authentication authentication)
            throws NotFoundException, UserHasNotPermission {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        Optional<CommunityEntity> communityOptional = communityRepo.findById(id);
        CommunityEntity community;
        UserEntity user;

        if (communityOptional.isEmpty()) throw new NotFoundException("Такой группы не существует");
        community = communityOptional.get();
        user = userRepo.findByLogin(authentication.getName());

        if (!community.getUsersSet().contains(user)) throw new UserHasNotPermission("У вас нет доступа к этой группе");
        for (CategoryEntity category : community.getCategoryEntities())
            categoryDTOS.add(CategoryDTO.toDTO(category));

        return categoryDTOS;
    }

    // Получение категории по id категории /category/id
    public CategoryDTO getCategory(Long id, Authentication authentication)
            throws NotFoundException, UserHasNotPermission {
        CategoryDTO categoryDTO;
        UserEntity user;
        CategoryEntity categoryEntity;
        Optional<CategoryEntity> categoryEntityOptional = categoryRepo.findById(id);

        if(categoryEntityOptional.isEmpty()) throw new NotFoundException("Такой категории не существует");
        categoryEntity = categoryEntityOptional.get();
        user = userRepo.findByLogin(authentication.getName());

        if (categoryEntity.getCommunity().getIsPrivate() && !categoryEntity.getCommunity().getUsersSet().contains(user))
            throw new UserHasNotPermission("Нет прав для доступа к группе");

        if (categoryEntity.getIsPrivate() && !categoryEntity.getUsers().contains(user))
            throw new UserHasNotPermission("Нет прав для доступа к категории");

        return CategoryDTO.toDTO(categoryEntity);
    }

    // создание категории /category

    // Удаление категории /category/id

    // изменение категории /category/id

    // Добавление/Удаление пользователя в категории

}
