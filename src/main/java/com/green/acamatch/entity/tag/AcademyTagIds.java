package com.green.acamatch.entity.tag;

import com.green.acamatch.entity.academy.Academy;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class AcademyTagIds {
    private Long acaId;
    private Long tagId;
}
