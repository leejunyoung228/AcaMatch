package com.green.acamatch.entity.academyCost;

import com.green.acamatch.entity.acaClass.AcaClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookId;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private AcaClass classId;

    private String bookPic;

    @Column(nullable = false)
    private String bookName;

    @Column(nullable = false)
    private int bookPrice;

    @Column(nullable = false)
    private String bookComment;

    @Column(nullable = false)
    private String manager;

    @Column(nullable = false)
    private int bookAmount;

    public void setClassId(Long classId) {
        this.classId = new AcaClass();
        this.classId.setClassId(classId);
    }
}
