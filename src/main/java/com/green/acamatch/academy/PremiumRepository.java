package com.green.acamatch.academy;

import com.green.acamatch.academy.premium.model.PremiumGetRes;
import com.green.acamatch.entity.academy.PremiumAcademy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PremiumRepository extends JpaRepository<PremiumAcademy, Long> {

    Optional<PremiumAcademy> findByAcademy_AcaId(Long academyId);


    //프리미엄 학원 승인 여부
    @Transactional
    @Modifying
    @Query(" update PremiumAcademy a set a.preCheck =:preCheck where a.acaId =:acaId")
    int updateByAcaId(@Param("acaId") Long acaId, @Param("preCheck") Integer preCheck);

    //프리미엄 시작일, 종료일
    @Transactional
    @Modifying
    @Query(" update PremiumAcademy a set a.startDate =:startDate, a.endDate =:endDate where a.acaId =:acaId")
    int updateDateByAcaId(@Param("acaId") Long acaId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    //프리미엄 학원 조회
    @Query(" SELECT new com.green.acamatch.academy.premium.model.PremiumGetRes(b.acaId, b.acaName, a.startDate, a.endDate, a.preCheck, c.bannerType, a.createdAt) " +
            "from PremiumAcademy as a join Academy as b on a.acaId = b.acaId left join Banner as c on a.acaId = c.acaId")
    List<PremiumGetRes> findAllByPremium();

    //프리미엄 학원 삭제
    @Transactional
    @Modifying
    @Query(" delete from PremiumAcademy a where a.acaId = :acaId")
    int deleteByAcaId(@Param("acaId") Long acaId);

    //프리미엄 종료 되기전 acaId 가져오기
    // endDate가 특정 날짜보다 이전인 acaId를 반환하는 메서드
    @Query("SELECT a.acaId FROM PremiumAcademy a WHERE a.endDate <= :now")
    List<Long> findAcaIdsByEndDateBefore(@Param("now") LocalDate now);

    //프리미엄 종료일 이후 삭제
    @Transactional
    @Modifying
    @Query(" delete from PremiumAcademy a where a.endDate <= :now")
    int deletePremiumAcademyByEndDate(@Param("now") LocalDate now);
}
