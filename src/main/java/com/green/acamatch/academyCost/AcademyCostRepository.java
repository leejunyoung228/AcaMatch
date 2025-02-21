package com.green.acamatch.academyCost;

import com.green.acamatch.entity.academyCost.AcademyCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyCostRepository extends JpaRepository<AcademyCost, Integer> {
}
