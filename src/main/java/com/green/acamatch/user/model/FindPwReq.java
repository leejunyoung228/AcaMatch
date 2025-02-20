package com.green.acamatch.user.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FindPwReq {
    @NotEmpty
    private String email;
}
