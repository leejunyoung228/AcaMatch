package com.green.acamatch.entity.myenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@RequiredArgsConstructor
public enum BannerPosition {
    TOP(1),
    BOTTOM(2),
    LEFT(3),
    RIGHT(4);

    private final int value;
}
