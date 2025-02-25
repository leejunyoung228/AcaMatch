package com.green.acamatch.entity.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne
    @MapsId("categoryTypeId")
    @JoinColumn(name = "category_type_id", nullable = false)
    private CategoryType categoryType;

    @Column(length = 50, nullable = false)
    private String categoryName;
}
