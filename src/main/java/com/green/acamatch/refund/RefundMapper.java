package com.green.acamatch.refund;

import com.green.acamatch.entity.academyCost.Refund;
import com.green.acamatch.refund.model.GetRefundListByAcaUserIdRes;
import com.green.acamatch.refund.model.GetRefundRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RefundMapper {
    List<GetRefundRes> getRefundList();
    List<GetRefundRes> getRefundListByUserId(long userId);
    GetRefundRes getInfoByRefundId(long refundId);
    List<GetRefundListByAcaUserIdRes> getRefundListByAcaUserId(long userId);
}
