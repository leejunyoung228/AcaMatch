package com.green.acamatch.entity.acaClass;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.Days;

@Setter
@Getter
@Entity
@Table(name = "classWeekDays")
public class ClassWeekDays {
    @EmbeddedId
    private ClassWeekDaysIds classWeekDaysIds;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class classId;

    @ManyToOne
    @MapsId("dayId")
    @JoinColumn(name = "day_id", nullable = false)
    private WeekDays day;
}
