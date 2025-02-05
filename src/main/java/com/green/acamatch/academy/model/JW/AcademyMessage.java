package com.green.acamatch.academy.model.JW;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class AcademyMessage {
    @Schema(title = "메세지", description = "성공 or 실패에 관한 메세지")
    private String message;
}
