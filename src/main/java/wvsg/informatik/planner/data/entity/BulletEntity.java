package wvsg.informatik.planner.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "bullets")
public class BulletEntity {
    public BulletEntity(String content, String type, int position, int indent, boolean isCompleted, UserEntity creator, ListEntity list) {
        this.content = content;
        this.type = type;
        this.position = position;
        this.isCompleted = isCompleted;
        this.creator = creator;
        this.list = list;
        this.indent = indent;
    }
    public BulletEntity(){
        this.content = "";
        this.type = "Dash";
        this.position = 0;
        this.indent = 0;
        this.isCompleted = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "content")
    @NotBlank
    private String content;

    @Column(name = "type")
    @NotBlank
    private String type;

    @Column(name = "position")
    private int position;

    @Column(name = "indent")
    private int indent;

    @Column(name = "isCompleted")
    private Boolean isCompleted;

    @OneToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private UserEntity creator;

    @OneToOne
    @JoinColumn(name = "list_id", referencedColumnName = "id")
    private ListEntity list;

    @ManyToOne
    @JoinColumn(name = "tags", referencedColumnName = "id")
    private TagEntity[] tags;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ListEntity getList() {
        return list;
    }

    public void setList(ListEntity list) {
        this.list = list;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public Boolean getIsCompleted() {
        if(isCompleted != null){
            return isCompleted;
        }
        return false;
    }

    public void setCompleted(Boolean b) {
        if(b == null){return;}
        this.isCompleted = b;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public TagEntity[] getTags() {
        return tags;
    }

    public void setTags(TagEntity[] tags) {
        this.tags = tags;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
