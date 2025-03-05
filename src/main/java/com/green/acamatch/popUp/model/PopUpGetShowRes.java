package com.green.acamatch.popUp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PopUpGetShowRes {
    private long popUpId;
    private String title;
    private String comment;
    private LocalDate startDate;
    private LocalDate endDate;
    private String popUpPic;
    @JsonIgnore
    private int popUpShow;
    private int popUpType;
}
