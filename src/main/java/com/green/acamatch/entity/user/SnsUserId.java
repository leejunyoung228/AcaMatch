package com.green.acamatch.entity.user;

import com.green.acamatch.entity.myenum.SignInProviderType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor
public class SnsUserId {
    @Column(name = "user_id")
    private Long userId;
    @Column
    private SignInProviderType signInProviderType;
}
