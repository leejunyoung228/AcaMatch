package com.green.acamatch.entity.like;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class LikeIds {
    private Long userId;
    private Long acaId;
}
