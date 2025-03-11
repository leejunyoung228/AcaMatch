package com.green.acamatch.book.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BookPostReq {
    @Schema(title = "책 이름", example = "책1")
    private String bookName;

    @Schema(title = "책 가격", example = "1000")
    private int bookPrice;

    @Schema(title = "책 소개", example = "책입니다~")
    private String bookComment;

    @Schema(title = "학원 담당자", example = "담당자1")
    private String manager;

    @Schema(title = "수업 PK", example = "1")
    private Long classId;

    @Schema(title = "책 수량", example = "100")
    private int bookAmount;
}
