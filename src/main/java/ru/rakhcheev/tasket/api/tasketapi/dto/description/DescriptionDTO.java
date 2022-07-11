package ru.rakhcheev.tasket.api.tasketapi.dto.description;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.rakhcheev.tasket.api.tasketapi.entity.DescriptionEntity;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class DescriptionDTO {

    private String name;
    private String surname;
    private String city;
    private String number;
    private String about;

    private DescriptionDTO() {
    }

    public static DescriptionDTO toDTO(DescriptionEntity descriptionEntity) {
        if (descriptionEntity == null) return null;
        DescriptionDTO description = new DescriptionDTO();
        description.setAbout(descriptionEntity.getAbout());
        description.setCity(descriptionEntity.getCity());
        description.setName(descriptionEntity.getName());
        description.setSurname(descriptionEntity.getSurname());
        return description;
    }

}
