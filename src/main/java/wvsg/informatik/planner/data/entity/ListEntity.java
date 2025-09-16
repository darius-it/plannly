package wvsg.informatik.planner.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "lists")
public class ListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "title")
    @NotBlank
    private String title;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private UserEntity owner;

    @Column(name = "is_daily_list")
    private boolean isDailyList;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getOwnerId() {
        return owner.getId();
    }

    public UserEntity getOwner(){
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public boolean isDailyList() {
        return isDailyList;
    }

    public void setDailyList(boolean dailyList) {
        isDailyList = dailyList;
    }

    public Boolean isPublic() {
        if(isPublic != null){
            return isPublic;
        }
        return false;
    }

    public void setPublic(Boolean isPublic) {
        if(isPublic != null){
            this.isPublic = isPublic;
        }
        this.isPublic = false;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
