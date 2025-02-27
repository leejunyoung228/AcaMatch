package com.green.acamatch.user.repository;

import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.entity.user.User;
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


    List<User> findByUserRole(UserRole userRole);

    boolean existsByUserId(long userId);

    @Query("SELECT u FROM User u JOIN JoinClass jc ON u.userId = jc.user.userId WHERE jc.classId = :acaClass" )
    List<User> findStudentsByClass(@Param("class") AcaClass acaClass);
}
