package com.green.acamatch.joinClass.model;

import com.green.acamatch.entity.joinClass.JoinClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinClassRepository extends JpaRepository<JoinClass, Long> {
}
