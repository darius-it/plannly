package wvsg.informatik.planner.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
@Table(name = "tags")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @OneToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private UserEntity creator;

}
