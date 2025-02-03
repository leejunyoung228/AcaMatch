package com.green.acamatch.entity.accesslog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ip;

    private LocalDateTime timeStamp;

    @PrePersist
    public void onCreate() {
        this.timeStamp = LocalDateTime.now();
    }


}
