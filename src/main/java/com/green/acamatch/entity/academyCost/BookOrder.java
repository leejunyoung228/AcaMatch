package com.green.acamatch.entity.academyCost;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BookOrder {
    @EmbeddedId
    private BookOrderIds bookOrderIds;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @MapsId("costId")
    @JoinColumn(name = "cost_id", nullable = false)
    private AcademyCost Academycost;
}
