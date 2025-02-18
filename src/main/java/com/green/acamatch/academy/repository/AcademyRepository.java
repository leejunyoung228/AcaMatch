package com.green.acamatch.academy.repository;

import com.green.acamatch.entity.academy.Academy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyRepository extends JpaRepository<Academy, Long> {

}
