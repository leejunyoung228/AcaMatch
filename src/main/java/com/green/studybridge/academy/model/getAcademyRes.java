package com.green.studybridge.academy.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class getAcademyRes {
    private long acaId;
    private String acaPic;
    private String acaName;
    private String address;
    private int star;
    private List<String> tagName;
}
