package com.green.studybridge.like.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
@Setter
@Getter
public class AcaDelLikeReq implements Serializable {
    private long userId;
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