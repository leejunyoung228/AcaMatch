package com.green.acamatch.entity.academyCost;

import com.green.acamatch.entity.acaClass.AcaClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @JoinColumn(name = "class_id")
    @ManyToOne
    private AcaClass classId;

    @JoinColumn(name = "book_id")
    @ManyToOne
    private Book BookId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int productPrice;

    public void setBookId(long bookId) {
        this.BookId = new Book();
        this.BookId.setBookId(bookId);
    }
}
