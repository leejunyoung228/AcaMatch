package com.green.acamatch.entity.reports;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionType {
    no_action("활동유지"),
    one_week_ban("7일 정지"),
    two_week_ban("14일 정지"),
    one_month_ban("한달 정지"),
    one_year_ban("일년 정지"),
    forever_ban("영구 정지");

    private final String description;
}
