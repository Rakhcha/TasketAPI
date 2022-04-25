package ru.rakhcheev.tasket.api.tasketapi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rakhcheev.tasket.api.tasketapi.entity.DescriptionEntity;

public interface DescriptionRepo extends CrudRepository<DescriptionEntity, Long> {



}
