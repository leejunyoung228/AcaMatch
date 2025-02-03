package com.green.acamatch.chat;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final ConcurrentHashMap<Long, Sinks.Many<String>> userSinks = new ConcurrentHashMap<>();

    // 📌 SSE 구독 (클라이언트에서 호출)
    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "sse 연결 받으시면 되요")
    public Flux<String> subscribe(@PathVariable Long userId) {
        Sinks.Many<String> sink = userSinks.computeIfAbsent(userId, k -> Sinks.many().multicast().onBackpressureBuffer());
        return sink.asFlux().delayElements(Duration.ofMillis(500));
    }

    // 📌 특정 유저에게 알림 전송
    public void sendNotification(Long userId, String message) {
        if (userSinks.containsKey(userId)) {
            userSinks.get(userId).tryEmitNext(message);
        }
    }
}
