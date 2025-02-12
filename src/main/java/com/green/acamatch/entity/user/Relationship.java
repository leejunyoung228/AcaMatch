package com.green.acamatch.entity.user;

import com.green.acamatch.entity.datetime.UpdatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Relationship extends UpdatedAt {
    @EmbeddedId
    private RelationshipIds id;

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
}
