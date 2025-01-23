package com.green.acamatch.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class RelationshipId {
    @Column(name = "parents_id")
    private Long parentsId;

    @Column(name = "student_id")
    private Long studentId;
}
