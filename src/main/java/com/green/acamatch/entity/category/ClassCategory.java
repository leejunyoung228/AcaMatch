package com.green.acamatch.entity.category;

import com.green.acamatch.entity.acaClass.AcaClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Setter
@Getter
@Entity
@Table(name = "class_category")
public class ClassCategory {
    @EmbeddedId
    private ClassCategoryIds classCategoryIds;

    @ManyToOne
    @MapsId("classId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "class_id", nullable = false)
    private AcaClass classId;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    private Category categoryId;
}
