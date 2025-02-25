package com.green.acamatch.entity.category;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class ClassCategoryIds implements Serializable {
    private Long classId;
    private Long categoryId;
}
