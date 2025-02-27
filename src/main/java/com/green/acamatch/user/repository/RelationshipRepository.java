package com.green.acamatch.user.repository;

import com.green.acamatch.entity.user.Relationship;
import com.green.acamatch.entity.user.RelationshipIds;
import com.green.acamatch.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipIds> {
    Optional<Relationship> findRelationshipByParentAndStudent(User parent, User student);

    Optional<Relationship> findRelationshipByParentAndStudentAndCertification(User parent, User student, int certification);

    List<Relationship> findRelationshipsByParent(User user);
    List<Relationship> findRelationshipsByStudent(User user);


    // 부모 ID, 학생 ID, 인증 여부로 보호자-학생 관계 조회
    @Query("SELECT r FROM Relationship r WHERE r.parent.userId = :parentId AND r.student.userId = :studentId AND r.certification = :certification")
    Optional<Relationship> findByParentUserIdAndStudentUserIdAndCertification(
            @Param("parentId") Long parentId,
            @Param("studentId") Long studentId,
            @Param("certification") int certification
    );
}

