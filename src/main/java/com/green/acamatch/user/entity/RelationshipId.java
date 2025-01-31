package com.green.acamatch.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 받는 생성자 추가
public class RelationshipId implements Serializable {
    @Column(name = "parents_id")
    private Long parentsId;

    @Column(name = "student_id")
    private Long studentId;
}
