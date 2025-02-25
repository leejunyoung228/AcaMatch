package com.green.acamatch.entity.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "category_type")
public class CategoryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryTypeId;

    @Column(length = 50, nullable = false)
    private String categoryTypeName;
}