package com.green.acamatch.academy.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddressDto {
    @NotEmpty
    @NotNull
    private String address;
    @NotEmpty
    @NotNull
    private String detailAddress;
    @NotEmpty
    @NotNull
    private String postNum;
}
