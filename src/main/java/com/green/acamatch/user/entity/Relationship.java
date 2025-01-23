package com.green.acamatch.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "relationship")
public class Relationship {
    @EmbeddedId
    private RelationshipId id;

    @ManyToOne
    @JoinColumn(name = "parents_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User parent;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User student;

    @Column(name = "certification", nullable = false, columnDefinition = "int default 0")
    private int certification;
}
