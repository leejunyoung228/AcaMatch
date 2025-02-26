package com.green.acamatch.entity.user;

import com.green.acamatch.entity.datetime.UpdatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
public class Relationship extends UpdatedAt {
    @EmbeddedId
    private RelationshipIds id;

    @ManyToOne
    @MapsId("parentsId")
    @JoinColumn(name = "parents_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User parent;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User student;

    @Column(name = "certification", nullable = false)
    private int certification = 0;
}
