package com.green.acamatch.book.model;

import com.green.acamatch.config.model.Paging;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBookListByAcaNameBookNameReq extends Paging {
    private String acaName;
    private String bookName;

    public GetBookListByAcaNameBookNameReq(Integer page, Integer size, String acaName, String bookName) {
        super(page, size);
        this.acaName = acaName;
        this.bookName = bookName;
    }
}
