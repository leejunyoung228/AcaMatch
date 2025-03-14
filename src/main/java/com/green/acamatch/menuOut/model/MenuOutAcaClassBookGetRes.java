package com.green.acamatch.menuOut.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MenuOutAcaClassBookGetRes {
    private Long acaId;
    private String acaName;
    private Long classId;
    private String className;
    private Long bookId;
    private String bookName;
}
