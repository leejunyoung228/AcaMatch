package com.green.acamatch.reports;

import com.green.acamatch.entity.reports.Reports;
import com.green.acamatch.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Long> {
}
