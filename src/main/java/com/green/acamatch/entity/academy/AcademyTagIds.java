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
public class AcademyTagIds implements Serializable {
    private Long acaId;
    private Long tagId;

}
