package com.green.acamatch.academy;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.location.Dong;
import com.green.acamatch.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AcademyRepository extends JpaRepository<Academy, Long> {

    List<Academy> findAllByUser(User user);

   /* @Transactional
    @Modifying
    @Query("UPDATE Academy a SET " +
           "a.acaName = CASE WHEN :acaName IS NOT NULL THEN :acaName ELSE a.acaName END," +
           "a.acaPhone = CASE WHEN :acaPhone IS NOT NULL THEN :acaPhone ELSE a.acaPhone END," +
           "a.acaComment = CASE WHEN :comment IS NOT NULL THEN :comment ELSE a.comment END," +
           "a.teacherNum = CASE WHEN :teacherNum IS NOT NULL THEN :teacherNum ELSE a.teacherNum END," +
           "a.acaName = CASE WHEN :acaName IS NOT NULL THEN :acaName ELSE a.acaName END," +
           "a.acaName = CASE WHEN :acaName IS NOT NULL THEN :acaName ELSE a.acaName END," +
           "a.acaName = CASE WHEN :acaName IS NOT NULL THEN :acaName ELSE a.acaName END," +
           "a.acaName = CASE WHEN :acaName IS NOT NULL THEN :acaName ELSE a.acaName END," +
           "a.acaName = CASE WHEN :acaName IS NOT NULL THEN :acaName ELSE a.acaName END," +
           "a.acaName = CASE WHEN :acaName IS NOT NULL THEN :acaName ELSE a.acaName END," +
           "a.acaName = CASE WHEN :acaName IS NOT NULL THEN :acaName ELSE a.acaName END," +
           "a.acaName = CASE WHEN :acaName IS NOT NULL THEN :acaName ELSE a.acaName END," +

    )
    int updateAcademyByAcaIdAndUserId(Long acaId, Long userId);*/

    //학원정보등록 승인 업데이트
    @Transactional
    @Modifying
    @Query(" update Academy a set a.acaAgree = 1 where a.acaId =:acaId")
    int updateAcademyAcaAgreeByAcaId(Long acaId);

    //프리미엄학원 승인 업데이트
    @Transactional
    @Modifying
    @Query(" update Academy a set a.premium = 1 where a.acaId =:acaId ")
    int updateAcademyPremiumByAcaId(Long acaId);

    //프리미엄 종료 후 아카데미 프리미엄 수정
    @Transactional
    @Modifying
    @Query(" update Academy a set a.premium = 0 where a.acaId =:acaId ")
    int updateAcademyPremiumEndByAcaId(Long acaId);

    //학원정도삭제
    @Transactional
    @Modifying
    @Query(" delete from Academy a where a.acaId=:acaId and a.user.userId=:userId")
    int deleteAcademyByAcaIdAndUserId(Long acaId, Long userId);

    @Transactional
    @Query(" SELECT COUNT(a) FROM Academy a WHERE a.acaId = :acaId")
    int checkAcaExists(Long acaId);

}
