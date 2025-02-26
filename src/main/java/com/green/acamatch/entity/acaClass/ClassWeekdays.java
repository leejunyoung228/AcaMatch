package com.green.acamatch.entity.acaClass;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Setter
@Getter
@Entity
@Table(name = "classWeekdays")
public class ClassWeekdays {
    @EmbeddedId
    private ClassWeekdaysIds classWeekdaysIds;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId("classId")
    @JoinColumn(name = "class_id", nullable = false)
    private AcaClass classId;

    @ManyToOne
    @MapsId("dayId")
    @JoinColumn(name = "day_id", nullable = false)
    private Weekdays day;
}
