package ru.rakhcheev.tasket.api.tasketapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "user")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DescriptionEntity description;

    @ManyToMany
    @JoinTable(name = "users_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "party_id"))
    private Set<CommunityEntity> groupSet;

    @OneToMany(mappedBy = "creator")
    private Set<CommunityEntity> setOfCreatedGroups;

    public UserEntity() {
    }

    public void setDescription(DescriptionEntity description) {
        if (description != null) description.setUser(this);
        this.description = description;
    }
}
