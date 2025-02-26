package com.green.acamatch.entity.acaClass;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ClassWeekdaysIds implements Serializable {
    private Long classId;
    private Long dayId;
}