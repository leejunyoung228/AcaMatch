package com.green.acamatch.entity.manager;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class TeacherIds implements Serializable {
    private Long userId;
    private Long acaId;
}
