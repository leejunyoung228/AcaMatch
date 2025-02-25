package com.green.acamatch.academy;

import com.green.acamatch.entity.academy.PremiumAcademy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface PremiumRepository extends JpaRepository<PremiumAcademy, Long> {

    @Transactional
    @Modifying
    @Query(" delete from PremiumAcademy a where a.endDate <= :now")
    int deletePremiumAcademyByEndDate(@Param("now") LocalDate now);
}
