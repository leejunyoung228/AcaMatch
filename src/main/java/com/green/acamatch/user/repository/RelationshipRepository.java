package com.green.acamatch.user.repository;

import com.green.acamatch.entity.user.Relationship;
import com.green.acamatch.entity.user.RelationshipId;
import com.green.acamatch.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipId> {
    Optional<Relationship> findRelationshipByParentAndStudent(User parent, User student);

    Optional<Relationship> findRelationshipByParentAndStudentAndCertification(User parent, User student, int certification);

    List<Relationship> findRelationshipsByParent(User user);

    List<Relationship> findRelationshipsByStudent(User user);
}
