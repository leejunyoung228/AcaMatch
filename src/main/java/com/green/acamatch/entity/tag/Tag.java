package com.green.acamatch.entity.tag;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(length = 30, nullable = false, unique = true)
    private String tagName;

    public Tag(String tagName) {
        this.tagName = tagName;
    }
}
