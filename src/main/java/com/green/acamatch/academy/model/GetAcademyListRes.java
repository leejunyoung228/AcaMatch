package com.green.acamatch.academy.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetAcademyListRes {
    private long acaId;
    private String acaPic;
    private String acaName;
    private String address;
    private double star;
    private AddressDto addressDto;
    private List<GetAcademyTagDto> tagName;
    private long totalCount;
}
