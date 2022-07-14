package ru.rakhcheev.tasket.api.tasketapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "description")
@Data
public class DescriptionEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "city")
    private String city;

    @Column(name = "number")
    private String number;

    @Column(name = "about")
    private String about;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private UserEntity user;

}
