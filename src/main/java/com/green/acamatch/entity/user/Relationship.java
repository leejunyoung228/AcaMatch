package com.green.acamatch.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Relationship{
    @EmbeddedId
    private RelationshipId id;

    @ManyToOne
    @MapsId("parentsId")
    @JoinColumn(name = "parents_id", insertable = false, updatable = false)
    private User parent;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private User student;

    @Column(name = "certification", nullable = false)
    private int certification = 0;
}
