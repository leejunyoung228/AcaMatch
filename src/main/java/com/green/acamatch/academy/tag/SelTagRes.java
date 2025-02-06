package com.green.acamatch.academy.tag;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class SelTagRes {
    private List<SelTagDto> selTagList;
}
