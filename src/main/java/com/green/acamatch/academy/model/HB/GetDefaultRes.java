package com.green.acamatch.academy.model.HB;

import com.green.acamatch.academy.model.AddressDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetDefaultRes {
    private long acaId;
    private String acaPic;
    private String acaName;
    private String address;
    private double star;
    private AddressDto addressDto;
    private List<GetAcademyTagDto> tagName;
    private long totalCount;
}
