package com.green.acamatch.entity.academy;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class AcademyPicIds implements Serializable {
    private Long acaId;

    @Column(length = 50, nullable = true)
    private String acaPic;

}
