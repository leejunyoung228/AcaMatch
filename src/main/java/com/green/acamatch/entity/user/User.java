package com.green.acamatch.entity.user;

import com.green.acamatch.entity.datetime.UpdatedAt;
import com.green.acamatch.entity.myenum.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity //테이블을 만들고 DML때 사용
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class User extends UpdatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    // 'role_id' 컬럼과 'role' 객체 연결
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private UserRole userRole;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 15, nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(length = 200, nullable = false, unique = true)
    private String nickName;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 100)
    private String upw;

    @Column()
    private String userPic;

    public User(Long userId) {
        this.userId = userId;
    }
}