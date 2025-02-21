package com.green.acamatch.academyCost;

import com.green.acamatch.entity.academyCost.AcademyCost;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademyCostRepository extends JpaRepository<AcademyCost, Integer> {
        @Query("SELECT a.tId FROM AcademyCost a WHERE a.orderId = :orderId")
        String findTidByOrderId(@Param("orderId") int orderId);

        @Query("SELECT a.userId FROM AcademyCost a WHERE a.orderId = :orderId")
        String findUserIdByOrderId(@Param("orderId") int orderId);
}
