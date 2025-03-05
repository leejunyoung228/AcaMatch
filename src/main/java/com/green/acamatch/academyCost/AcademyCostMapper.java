package com.green.acamatch.academyCost;

import com.green.acamatch.academyCost.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcademyCostMapper {
    GetAcademyCostInfoRes getInfoByTid(String tid);

    long getBookIdByProductId(long productId);

    long getProductIdByBookId(long bookId);

    GetAcademyCostInfoByMonth getAcademyCostInfo();

    List<GetSettlementListRes> getSettlementList(GetSettlementListReq req);

    GetAcademyCostInfoByCostId getAcademyCostInfoByCostId(long costId);
}
