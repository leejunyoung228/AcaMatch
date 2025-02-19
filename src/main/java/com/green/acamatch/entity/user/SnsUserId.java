package com.green.acamatch.entity.user;

import com.green.acamatch.entity.myenum.SignInProviderType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = { "sign_in_provider_type", "sns_user_id" }
                )
        }
)
public class SnsUserId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private SignInProviderType signInProviderType;

    @Column(nullable = false)
    private String snsUserId;

}
