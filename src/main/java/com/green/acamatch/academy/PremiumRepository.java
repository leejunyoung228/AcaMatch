package com.green.acamatch.academy;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.PremiumAcademy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PremiumRepository extends JpaRepository<PremiumAcademy, Long> {

    Optional<PremiumAcademy> findByAcademy_AcaId(Long academyId);

    @Transactional
    @Modifying
    @Query(" update PremiumAcademy a set a.preCheck =:preCheck where a.acaId =:acaId")
    int updateByAcaId(@Param("acaId") Long acaId, @Param("preCheck") Integer preCheck);

    @Transactional
    @Modifying
    @Query(" update PremiumAcademy a set a.startDate =:startDate, a.endDate =:endDate where a.acaId =:acaId")
    int updateDateByAcaId(@Param("acaId") Long acaId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Transactional
    @Modifying
    @Query(" delete from PremiumAcademy a where a.acaId = :acaId")
    int deleteByAcaId(@Param("acaId") Long acaId);


    @Transactional
    @Modifying
    @Query(" delete from PremiumAcademy a where a.endDate <= :now")
    int deletePremiumAcademyByEndDate(@Param("now") LocalDate now);
}
