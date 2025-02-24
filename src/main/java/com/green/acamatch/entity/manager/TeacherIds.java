package com.green.acamatch.entity.manager;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class TeacherIds {
    private Long userId;
    private Long acaId;
}
