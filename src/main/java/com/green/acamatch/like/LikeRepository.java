package com.green.acamatch.like;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.like.Like;
import com.green.acamatch.entity.like.LikeIds;
import com.green.acamatch.entity.popUp.PopUp;
import com.green.acamatch.like.dto.AcademyLikedUsersDto;
import com.green.acamatch.like.dto.LikedAcademyDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeIds> {

    @Query("SELECT new com.green.acamatch.like.dto.LikedAcademyDto("
            + "a.acaId, a.acaName, "
            + "(SELECT COUNT(1) FROM Like l WHERE l.likeIds.userId = :userId AND l.likeIds.acaId = a.acaId), "
            + "(SELECT COUNT(1) FROM Like l WHERE l.likeIds.acaId = a.acaId)) "
            + "FROM Academy a WHERE a.acaId IN :academyIds")
    List<LikedAcademyDto> findLikedAcademiesByUserId(@Param("userId") Long userId, @Param("academyIds") List<Long> academyIds);



    @Query("SELECT new com.green.acamatch.like.dto.AcademyLikedUsersDto(" +
            "a.acaId, a.acaName, " +
            "(SELECT COUNT(l) FROM Like l WHERE l.academy.acaId = a.acaId)) " +
            "FROM Academy a " +
            "WHERE a.acaId IN :academyIds")
    List<AcademyLikedUsersDto> findAllOwnedAcademyLikes(@Param("academyIds") List<Long> academyIds);


    @Query("SELECT new com.green.acamatch.like.dto.AcademyLikedUsersDto(" +
            "a.acaId, a.acaName, COUNT(l)) " +
            "FROM Like l JOIN l.academy a " +
            "WHERE a.acaId IN :academyIds " +
            "GROUP BY a.acaId, a.acaName")
    List<AcademyLikedUsersDto> findAcademiesWithLikeCounts(@Param("academyIds") List<Long> academyIds);

    @Query("SELECT l.likeIds.acaId FROM Like l WHERE l.likeIds.userId = :userId")
    List<Long> findLikedAcademyIdsByUserId(@Param("userId") Long userId);

}
