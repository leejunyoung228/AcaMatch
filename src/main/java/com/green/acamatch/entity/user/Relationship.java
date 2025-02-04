package com.green.acamatch.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Relationship{
    @EmbeddedId
    private RelationshipId id;

    @ManyToOne
    @MapsId("parentsId")
    @JoinColumn(name = "parents_id")
    private User parent;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private User student;

    @Column(name = "certification", nullable = false)
    private int certification = 0;

    private LocalDateTime updatedAt;
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
