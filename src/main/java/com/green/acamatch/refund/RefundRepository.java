package com.green.acamatch.refund;

import com.green.acamatch.entity.academyCost.Refund;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RefundRepository extends JpaRepository<Refund, Integer> {
    @Modifying
    @Query("UPDATE Refund r SET r.refundStatus = 1 WHERE r.refundId = :refundId")
    int updateRefundStatusToApproved(@Param("refundId") Long refundId);
}
