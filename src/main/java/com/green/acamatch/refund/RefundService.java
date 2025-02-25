package com.green.acamatch.refund;

import com.green.acamatch.entity.academyCost.Refund;
import com.green.acamatch.refund.model.GetRefundRes;
import com.green.acamatch.refund.model.PostRefundReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundService {
    private final RefundRepository refundRepository;
    private final RefundMapper refundMapper;

    public int postRefund(PostRefundReq req){
        Refund refund = new Refund();
        refund.setAcademyCost(req.getCostId());
        if(req.getRefundComment() != null){
            refund.setRefundComment(req.getRefundComment());
        }
        refund.setRefundStatus(0);
        refundRepository.save(refund);
        return 1;
    }

    public List<GetRefundRes> getRefundRes(){
        List<GetRefundRes> res = refundMapper.getRefundList();
        return res;
    }

    public List<GetRefundRes> getRefundResList(long userId){
        List<GetRefundRes> res = refundMapper.getRefundListByUserId(userId);
        return res;
    }

    public int updateRefund(Long refundId){
        int result = refundRepository.updateRefundStatusToApproved(refundId);
        return 1;
    }
}
