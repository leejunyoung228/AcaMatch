package com.green.acamatch.manager;

import com.green.acamatch.manager.model.GetAcademyCostCountRes;
import com.green.acamatch.manager.model.GetAcademyCountRes;
import com.green.acamatch.manager.model.GetUserCountRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManagerMapper {
    List<GetAcademyCountRes> getAcademyCount(String month);
    List<GetUserCountRes> getUserCount(String month);
    List<GetAcademyCostCountRes> getAcademyCostCount(String month);
}