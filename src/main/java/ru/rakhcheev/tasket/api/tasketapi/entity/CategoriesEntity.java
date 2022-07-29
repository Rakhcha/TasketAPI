package ru.rakhcheev.tasket.api.tasketapi.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "categories")
public class CategoriesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    private UserEntity leader;

    // задания

    // пользователи

}
