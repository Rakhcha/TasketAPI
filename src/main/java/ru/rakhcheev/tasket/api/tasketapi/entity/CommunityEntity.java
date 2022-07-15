package ru.rakhcheev.tasket.api.tasketapi.entity;

import lombok.Data;

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
    private Long communityId;

    @Column(name = "name", nullable = false)
    private String communityName;

    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;

    // TODO Разобраться с каскадом
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;

    @Column(name = "status_activity", nullable = false)
    private EntityStatusEnum statusActivity;

    // TODO Разобраться с каскадом
    @ManyToMany
    @JoinTable(name = "users_groups",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> usersSet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunityEntity that = (CommunityEntity) o;
        return communityId.equals(that.communityId) && communityName.equals(that.communityName) && isPrivate.equals(that.isPrivate) && creator.getLogin().equals(that.creator.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(communityId, communityName, isPrivate, creator.getLogin());
    }
}
