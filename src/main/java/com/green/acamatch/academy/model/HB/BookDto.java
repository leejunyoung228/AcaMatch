package com.green.acamatch.academy.model.HB;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    private long bookId;
    private String bookName;
    private int bookAmount;
    private String bookComment;
    private String bookPic;
    private int bookPrice;
    private String manager;
    private long classId;
    private long productId;
}
