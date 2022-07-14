package ru.rakhcheev.tasket.api.tasketapi.entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "user")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DescriptionEntity description;

    // TODO Разобраться с каскадом
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private Set<CommunityEntity> groupSet;

    // TODO Разобраться с каскадом
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private Set<CommunityEntity> setOfCreatedGroups;

    public UserEntity() {
    }

    public void setDescription(DescriptionEntity description) {
        if (description != null) description.setUser(this);
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id.equals(that.id) && login.equals(that.login) && password.equals(that.password) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, email);
    }
}
