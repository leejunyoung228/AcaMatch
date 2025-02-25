package com.green.acamatch.entity.acaClass;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "classWeekdays")
public class ClassWeekdays {
    @EmbeddedId
    private ClassWeekdaysIds classWeekdaysIds;

    @ManyToOne
    @MapsId("classId")
    @JoinColumn(name = "class_id", nullable = false)
    private AcaClass classId;

    @ManyToOne
    @MapsId("dayId")
    @JoinColumn(name = "day_id", nullable = false)
    private Weekdays day;
}
