package com.green.acamatch.book;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@EqualsAndHashCode
public class BookMessage {
    private String message;
}
