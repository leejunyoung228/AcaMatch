package com.green.acamatch.user.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationshipReq {
    @NotEmpty
    private String email;
}
