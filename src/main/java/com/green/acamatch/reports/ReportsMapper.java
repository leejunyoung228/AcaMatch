package com.green.acamatch.reports;

import com.green.acamatch.reports.model.GetAcademyListReq;
import com.green.acamatch.reports.model.GetAcademyListRes;
import com.green.acamatch.reports.model.GetUserListRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportsMapper {
    List<GetUserListRes> getUserList();
    List<GetAcademyListRes> getAcademyList(GetAcademyListReq req);
}
