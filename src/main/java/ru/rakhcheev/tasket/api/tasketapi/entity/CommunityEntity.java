package ru.rakhcheev.tasket.api.tasketapi.entity;

import lombok.Data;
import ru.rakhcheev.tasket.api.tasketapi.entity.enums.EntityStatusEnum;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "community")
public class CommunityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String communityName;

    @Column(name = "is_private")
    private Boolean isPrivate;

    // TODO Разобраться с каскадом
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;

    @Column(name = "status_activity")
    private EntityStatusEnum statusActivity;

    // TODO Разобраться с каскадом
    @ManyToMany
    @JoinTable(name = "group_user",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> usersSet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunityEntity that = (CommunityEntity) o;
        return id.equals(that.id) && communityName.equals(that.communityName) && isPrivate.equals(that.isPrivate) && creator.getLogin().equals(that.creator.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, communityName, isPrivate, creator.getLogin());
    }
}
