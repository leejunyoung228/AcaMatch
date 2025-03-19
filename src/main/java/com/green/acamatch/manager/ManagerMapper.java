package com.green.acamatch.manager;

import com.green.acamatch.manager.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManagerMapper {
    List<GetAcademyCountRes> getAcademyCount(GetAcademyCountReq req);

    List<GetUserCountRes> getUserCount(GetUserCountReq req);

    List<GetAcademyCostCountRes> getAcademyCostCount(GetAcademyCostCountReq req);

    List<GetAcademyCostByUserIdRes> getAcademyCostByUserId(GetAcademyCostByUserIdReq req);

    List<GetUserCountByUserIdRes> getUserCountByUserId(GetUserCountByUserIdReq req);

    List<GetUserInfoListRes> GetUserInfoList(GetUserInfoListReq req);

    GetAcademyCostInfoByUserId GetAcademyCostInfoByUserId(long userId);
}

