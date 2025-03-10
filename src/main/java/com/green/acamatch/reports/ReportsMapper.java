package com.green.acamatch.reports;

import com.green.acamatch.reports.model.GetUserListRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportsMapper {
    List<GetUserListRes> getUserList();
}
