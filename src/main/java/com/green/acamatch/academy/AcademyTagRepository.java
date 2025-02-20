package com.green.acamatch.academy;

import com.green.acamatch.entity.academy.Academy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyTagRepository extends JpaRepository<Academy, Long> {
}
