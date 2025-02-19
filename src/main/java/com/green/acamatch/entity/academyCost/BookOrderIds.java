package com.green.acamatch.entity.academyCost;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class BookOrderIds implements Serializable {
    private Long bookId;
    private Long costId;
}
