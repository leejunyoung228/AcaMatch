package com.green.studybridge.like.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
@Setter
@Getter
public class AcaDelLikeReq implements Serializable {
    @Schema(title = "유저 PK", example = "2119", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
    @Schema(title = "학원 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcaDelLikeReq delLike = (AcaDelLikeReq) o;
        return Objects.equals(userId, delLike.userId) && Objects.equals(acaId, delLike.acaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, acaId);
    }
}