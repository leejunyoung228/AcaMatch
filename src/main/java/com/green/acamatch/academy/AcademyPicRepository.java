package com.green.acamatch.academy;

import com.green.acamatch.entity.academy.Academy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyPicRepository extends JpaRepository<Academy, Long> {
}
