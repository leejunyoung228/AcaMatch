package com.green.acamatch.entity.academyCost;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BookOrder {
    @EmbeddedId
    private BookOrderIds bookOrderIds;
}
