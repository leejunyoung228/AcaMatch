package com.green.acamatch.academyCost;

import com.green.acamatch.entity.academyCost.AcademyCost;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AcademyCostRepository extends JpaRepository<AcademyCost, Integer> {
        @Query("SELECT a.tId FROM AcademyCost a WHERE a.orderId = :orderId")
        String findTidByOrderId(@Param("orderId") int orderId);

        @Query("SELECT a.userId FROM AcademyCost a WHERE a.orderId = :orderId")
        String findUserIdByOrderId(@Param("orderId") int orderId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE academy_cost SET cost_status = :status WHERE order_id = :orderId", nativeQuery = true)
        int updateStatusNative(@Param("status") int status, @Param("orderId") int orderId);

        @Query("SELECT a FROM AcademyCost a WHERE a.createdAt >= :oneMonthAgo")
        List<AcademyCost> findRecentPayments(@Param("oneMonthAgo") LocalDateTime oneMonthAgo);

        @Query("SELECT a FROM AcademyCost a WHERE a.userId = :userId AND a.createdAt >= :oneMonthAgo")
        List<AcademyCost> findRecentPaymentsByUserId(@Param("userId") Long userId,
                                                     @Param("oneMonthAgo") LocalDateTime oneMonthAgo);
}
