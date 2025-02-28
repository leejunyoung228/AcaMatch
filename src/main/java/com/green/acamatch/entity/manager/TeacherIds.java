package com.green.acamatch.entity.manager;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TeacherIds implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "aca_id")
    private Long acaId;
}