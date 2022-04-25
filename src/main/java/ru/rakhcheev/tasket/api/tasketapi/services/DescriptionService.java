package ru.rakhcheev.tasket.api.tasketapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rakhcheev.tasket.api.tasketapi.entity.DescriptionEntity;
import ru.rakhcheev.tasket.api.tasketapi.exception.DescriptionIsNotFoundException;
import ru.rakhcheev.tasket.api.tasketapi.exception.DescriptionTableIsEmptyException;
import ru.rakhcheev.tasket.api.tasketapi.repository.DescriptionRepo;

import java.util.Optional;

@Service
public class DescriptionService {

    @Autowired
    private DescriptionRepo descriptionRepo;

    public DescriptionEntity getByUserId(Long user_id) throws DescriptionTableIsEmptyException, DescriptionIsNotFoundException {
        return getDescriptionFromDatabase(user_id);
    }

    public void updateDescription(String id, DescriptionEntity newDescriptionEntity) throws DescriptionTableIsEmptyException, DescriptionIsNotFoundException{
        DescriptionEntity entity = getDescriptionFromDatabase(Long.parseLong(id));
        if(newDescriptionEntity.getAbout() != null) entity.setAbout(newDescriptionEntity.getAbout());
        if(newDescriptionEntity.getCity() != null) entity.setCity(newDescriptionEntity.getCity());
        if(newDescriptionEntity.getName() != null) entity.setName(newDescriptionEntity.getName());
        if(newDescriptionEntity.getPatronymic() != null) entity.setPatronymic(newDescriptionEntity.getPatronymic());
        if(newDescriptionEntity.getSurname() != null) entity.setSurname(newDescriptionEntity.getSurname());
        if(newDescriptionEntity.getPhone_number() != null) entity.setPhone_number(newDescriptionEntity.getPatronymic());
        descriptionRepo.save(entity);
    }

    private DescriptionEntity getDescriptionFromDatabase(Long user_id) throws DescriptionTableIsEmptyException, DescriptionIsNotFoundException {
        if (!descriptionRepo.findAll().iterator().hasNext()) throw new DescriptionTableIsEmptyException("База данных Информации о пользователях пуста");
        Optional<DescriptionEntity> descriptionEntityOptional = descriptionRepo.findById(user_id);
        if(descriptionEntityOptional.isEmpty()) throw new DescriptionIsNotFoundException("Информации о данном пользователе нет (пользователя с таким id не существует)");
        return descriptionEntityOptional.get();
    }

}
