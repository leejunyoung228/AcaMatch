package com.green.acamatch.academy.mapper;

import com.green.acamatch.academy.premium.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PremiumMapper {
    List<PremiumBannerExistGetRes> getPremiumBannerExist(PremiumBannerExistGetReq req);
    List<PremiumNotexistGetRes> getPremiumNotExist(PremiumNotExistGetReq req);

    List<PremiumAcaAdminGetRes> getPremiumAcademyAcaAdmin(PremiumAcaAdminGetReq req);
}
