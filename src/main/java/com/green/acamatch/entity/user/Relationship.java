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

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
