package com.green.acamatch.refund;

import com.green.acamatch.academyCost.AcademyCostRepository;
import com.green.acamatch.entity.academyCost.AcademyCost;
import com.green.acamatch.entity.academyCost.Refund;
import com.green.acamatch.refund.model.GetRefundListByAcaUserIdRes;
import com.green.acamatch.refund.model.GetRefundRes;
import com.green.acamatch.refund.model.PostRefundReq;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundService {
    private final RefundRepository refundRepository;
    private final RefundMapper refundMapper;
    private final AcademyCostRepository academyCostRepository;

    public int postRefund(PostRefundReq req){
        AcademyCost academyCost = academyCostRepository.findById(req.getCostId()).orElse(null);
        Refund refund = new Refund();
        refund.setAcademyCost(req.getCostId());
        refund.setTid(academyCost.getTId());
        if(req.getRefundComment() != null){
            refund.setRefundComment(req.getRefundComment());
        }
        refund.setRefundStatus(0);
        refund.setCreatedAt(LocalDate.now());
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

    @Transactional  // Service 레이어에서 트랜잭션 관리
    public void updateRefund(Long refundId) {
        int updatedRows = refundRepository.updateRefundStatusToApproved(refundId);
        if (updatedRows == 0) {
            throw new EntityNotFoundException("해당 refundId에 대한 환불 정보가 없습니다: " + refundId);
        }
    }

    public List<GetRefundListByAcaUserIdRes> getRefundListByAcaUserId(long userId){
        return refundMapper.getRefundListByAcaUserId(userId);
    }
}
