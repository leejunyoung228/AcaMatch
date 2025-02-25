package com.green.acamatch.refund;

import com.green.acamatch.entity.academyCost.Refund;
import com.green.acamatch.refund.model.PostRefundReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {
    private final RefundRepository refundRepository;

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


}
