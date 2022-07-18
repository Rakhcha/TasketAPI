package ru.rakhcheev.tasket.api.tasketapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "community_url")
public class CommunityUrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "param", nullable = false)
    private String urlParam;

    @Column(name = "date")
    private LocalDateTime destroyDate;

    @Column(name = "once_used", nullable = false)
    private Boolean onceUsed;

    @Column(name = "status", nullable = false)
    private EntityStatusEnum status;

    @ManyToOne()
    @JoinColumn(name = "community_id", nullable = false)
    private CommunityEntity community;

    public CommunityUrlEntity() {
        this.urlParam = UUID.randomUUID().toString().replace("-", "");
        this.onceUsed = false;
        this.status = EntityStatusEnum.ACTIVE;
    }

    public CommunityUrlEntity(CommunityEntity community) {
        this.urlParam = UUID.randomUUID().toString().replace("-", "");
        this.onceUsed = false;
        this.status = EntityStatusEnum.ACTIVE;
        this.community = community;
    }

    public void regenerateUrlParam() {
        this.urlParam = UUID.randomUUID().toString().replace("-", "");
    }

}
