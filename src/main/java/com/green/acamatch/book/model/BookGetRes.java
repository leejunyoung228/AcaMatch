package com.green.acamatch.book.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookGetRes {
    @Schema(title = "책 PK")
    private long bookId;

    @Schema(title = "책 이름")
    private String bookName;

    @Schema(title = "책 사진")
    private String bookPic;

    @Schema(title = "책 가격")
    private int bookPrice;

    @Schema(title = "책 소개")
    private String bookComment;

    @Schema(title = "책 등록한 사람")
    private String manager;

    @Schema(title = "책이 등록된 수업")
    private Long classId;

    @Schema(title = "책 재고 수량")
    private int bookAmount;
}
