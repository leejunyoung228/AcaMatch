package com.green.acamatch.accessLog;

import com.green.acamatch.entity.accesslog.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    List<AccessLog> findByTimeStampAfter(LocalDateTime oneDayAgo);
}
