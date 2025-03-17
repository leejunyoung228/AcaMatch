package com.green.acamatch.reports;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.reports.Reports;
import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Long> {
    @Query("SELECT r FROM Reports r WHERE r.academy = : academy")
    Reports findByAcaId(@RequestParam Academy academy);

    @Query("SELECT r FROM Reports r WHERE r.reportedUser = : user")
    Reports findByUser(@RequestParam User user);
}
