package com.green.acamatch.academyCost;

import com.green.acamatch.academyCost.model.GetAcademyCostInfoRes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AcademyCostMapper {
    GetAcademyCostInfoRes getInfoByTid (String tid);
    long getBookIdByProductId(long productId);
}
