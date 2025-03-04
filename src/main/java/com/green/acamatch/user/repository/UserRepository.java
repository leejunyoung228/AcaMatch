package com.green.acamatch.user.repository;

import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.model.UserReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    Optional<User> findByEmail(String email);


    Optional<User> findByUserIdAndUserRole(Long userId, UserRole userRole);

    Optional<User> findByUserId(long userId);

    boolean existsByUserIdAndUserRole(Long userId, UserRole role);

    boolean existsByUserId(long userId);

    @Query("SELECT u FROM User u JOIN JoinClass jc ON u.userId = jc.user.userId WHERE jc.acaClass = :acaClass")
    List<User> findStudentsByClass(@Param("acaClass") AcaClass acaClass);

    // 모든 사용자 정보 및 신고 횟수를 조회
    @Query("SELECT u.userId AS userId, u.userRole AS userRole, u.name AS name, " +
            "u.email AS email, u.phone AS phone, u.birth AS birth, " +
            "u.nickName AS nickName, u.userPic AS userPic, " +
            "u.createdAt AS createdAt, u.updatedAt AS updatedAt, COALESCE(COUNT(r), 0) AS reportsCount " +
            "FROM User u LEFT JOIN Reports r ON u.userId = r.user.userId " +
            "WHERE u.userId != 1 " +  // userId 1 제외
            "GROUP BY u.userId")
    List<UserReportProjection> findUsersExceptAdmin(); // Optional 제거


    @Query("SELECT u.userId AS userId, u.userRole AS userRole, u.name AS name, " +
            "u.email AS email, u.phone AS phone, u.birth AS birth, " +
            "u.nickName AS nickName, u.userPic AS userPic, " +
            "u.createdAt AS createdAt, u.updatedAt AS updatedAt, COALESCE(COUNT(r), 0) AS reportsCount " +
            "FROM User u LEFT JOIN Reports r ON u.userId = r.user.userId " +
            "WHERE (:userId IS NULL OR u.userId = :userId) " +  // userId 조건 추가 (NULL이면 전체 조회)
            "AND (:name IS NULL OR u.name LIKE %:name%) " +  // userName 검색 (부분 일치 가능)
            "AND (:userRole IS NULL OR u.userRole = :userRole) " +  // userRole 조건 (NULL이면 전체 조회)
            "AND u.userId != 1 " +  // userId가 1이 아닌 경우만 조회
            "GROUP BY u.userId")
    List<UserReportProjection> findUsersWithFilters(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("userRole") UserRole userRole
    );



}
