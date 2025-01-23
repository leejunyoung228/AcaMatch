package com.green.acamatch.joinClass.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinClassDto {
    @JsonIgnore
    private long acaId;
    @JsonIgnore
    private long classId;

    private String acaPic;
    private String acaName;
    private String className;
}
