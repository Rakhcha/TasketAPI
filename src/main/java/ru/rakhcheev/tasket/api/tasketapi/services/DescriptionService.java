package ru.rakhcheev.tasket.api.tasketapi.services;

import org.springframework.stereotype.Service;
import ru.rakhcheev.tasket.api.tasketapi.entity.DescriptionEntity;
import ru.rakhcheev.tasket.api.tasketapi.exception.DescriptionIsNotFoundException;
import ru.rakhcheev.tasket.api.tasketapi.exception.TableIsEmptyException;
import ru.rakhcheev.tasket.api.tasketapi.repository.DescriptionRepo;
import ru.rakhcheev.tasket.api.tasketapi.repository.UserRepo;

import java.util.Optional;

@Service
public class DescriptionService {

    private final DescriptionRepo descriptionRepo;
    private final UserRepo userRepo;

    public DescriptionService(DescriptionRepo descriptionRepo, UserRepo userRepo) {
        this.descriptionRepo = descriptionRepo;
        this.userRepo = userRepo;
    }

    public DescriptionEntity getByUserId(Long user_id) throws TableIsEmptyException, DescriptionIsNotFoundException {
        return getDescriptionFromDatabase(user_id);
    }

    public void updateDescriptionByLogin(String login, DescriptionEntity newDescriptionEntity) throws TableIsEmptyException, DescriptionIsNotFoundException {
        Long id = userRepo.findByLogin(login).getId();
        updateDescription(id, newDescriptionEntity);
    }

    public void updateDescriptionById(Long id, DescriptionEntity newDescriptionEntity) throws TableIsEmptyException, DescriptionIsNotFoundException {
        updateDescription(id, newDescriptionEntity);
    }


    private void updateDescription(Long id, DescriptionEntity newDescriptionEntity) throws TableIsEmptyException, DescriptionIsNotFoundException {
        DescriptionEntity entity = getDescriptionFromDatabase(id);
        if (newDescriptionEntity.getAbout() != null) entity.setAbout(newDescriptionEntity.getAbout());
        if (newDescriptionEntity.getCity() != null) entity.setCity(newDescriptionEntity.getCity());
        if (newDescriptionEntity.getName() != null) entity.setName(newDescriptionEntity.getName());
        if (newDescriptionEntity.getSurname() != null) entity.setSurname(newDescriptionEntity.getSurname());
        if (newDescriptionEntity.getNumber() != null) entity.setNumber(newDescriptionEntity.getNumber());
        descriptionRepo.save(entity);
    }

    private DescriptionEntity getDescriptionFromDatabase(Long user_id) throws TableIsEmptyException, DescriptionIsNotFoundException {
        if (!descriptionRepo.findAll().iterator().hasNext())
            throw new TableIsEmptyException("Информации о пользователях не существует.");
        Optional<DescriptionEntity> descriptionEntityOptional = descriptionRepo.findById(user_id);
        if (descriptionEntityOptional.isEmpty())
            throw new DescriptionIsNotFoundException("Информации о данном пользователе нет (Пользователя с таким id не существует).");
        return descriptionEntityOptional.get();
    }

}
