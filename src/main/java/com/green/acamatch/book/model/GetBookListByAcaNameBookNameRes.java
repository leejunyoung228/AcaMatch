package com.green.acamatch.book.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBookListByAcaNameBookNameRes {
    private long bookId;
    private String bookName;
    private String bookComment;
    private String bookPic;
    private int bookAmount;
    private int bookPrice;
    private String manager;
    private String createdAt;
    private long acaId;
    private String acaName;
    private int bookCount;
}
