package com.green.acamatch.entity.user;

import com.green.acamatch.entity.myenum.SignInProviderType;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor
public class SnsUserId {
    private Long userId;
    private SignInProviderType signInProviderType;
}
