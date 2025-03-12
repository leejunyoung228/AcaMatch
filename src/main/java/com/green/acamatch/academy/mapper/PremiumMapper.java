package com.green.acamatch.academy.mapper;

import com.green.acamatch.academy.premium.model.PremiumBannerExistGetReq;
import com.green.acamatch.academy.premium.model.PremiumBannerExistGetRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PremiumMapper {
    List<PremiumBannerExistGetRes> getPremiumBannerExist(PremiumBannerExistGetReq req);
}
