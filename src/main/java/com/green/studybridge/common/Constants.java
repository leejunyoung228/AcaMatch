package com.green.studybridge.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
    @Getter
    private static int default_page_size;

    public Constants(@Value("${const.default-page-size}") int default_page_size) {
        Constants.default_page_size = default_page_size;
    }
}
