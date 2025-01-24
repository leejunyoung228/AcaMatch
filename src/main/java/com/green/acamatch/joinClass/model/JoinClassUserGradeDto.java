package com.green.acamatch.joinClass.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JoinClassUserGradeDto {
    @Schema(title = "유저 사진")
    private String userPic;
    @Schema(title = "유저 이름", example = "홍길동")
    private String userName;

    private List<JoinClassExamDto> examDtoList;
    @JsonIgnore
    private int scoreType;
}
