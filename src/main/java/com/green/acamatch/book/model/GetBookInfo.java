package com.green.acamatch.book.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBookInfo {
    private long bookId;
    private String bookName;
    private String bookComment;
    private int bookAmount;
    private String bookPic;
    private int bookPrice;
    private String manager;
    private long classId;
    private String createdAt;
    private long productId;
}
