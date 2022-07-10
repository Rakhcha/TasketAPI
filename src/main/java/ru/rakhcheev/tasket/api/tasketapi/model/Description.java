package ru.rakhcheev.tasket.api.tasketapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.rakhcheev.tasket.api.tasketapi.entity.DescriptionEntity;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class Description {

    private String name;
    private String surname;
    private String patronymic;
    private String city;
    private String phone_number;
    private String about;

    private Description() {
    }

    public static Description toModel(DescriptionEntity descriptionEntity) {
        if (descriptionEntity == null) return null;
        Description description = new Description();
        description.setAbout(descriptionEntity.getAbout());
        description.setCity(descriptionEntity.getCity());
        description.setName(descriptionEntity.getName());
        description.setSurname(descriptionEntity.getSurname());
        description.setPhone_number(descriptionEntity.getNumber());
        return description;
    }

}
