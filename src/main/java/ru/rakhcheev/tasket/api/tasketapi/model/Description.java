package ru.rakhcheev.tasket.api.tasketapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.rakhcheev.tasket.api.tasketapi.entity.DescriptionEntity;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Description {

    private String name;
    private String surname;
    private String patronymic;
    private String city;
    private String phone_number;
    private String about;

    public Description() {
    }

    public static Description toModel(DescriptionEntity descriptionEntity){
        if(descriptionEntity == null) return null;
        Description description = new Description();
        description.setAbout(descriptionEntity.getAbout());
        description.setCity(descriptionEntity.getCity());
        description.setName(descriptionEntity.getName());
        description.setPatronymic(descriptionEntity.getPatronymic());
        description.setSurname(descriptionEntity.getSurname());
        description.setPhone_number(descriptionEntity.getPhone_number());
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
