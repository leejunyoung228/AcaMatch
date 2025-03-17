package com.green.acamatch.reports;

import com.green.acamatch.reports.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportsMapper {
    List<GetUserListRes> getUserList(GetUserListReq req);
    List<GetAcademyListRes> getAcademyList(GetAcademyListReq req);
    List<GetReviewListRes> getReviewList(GetReviewListReq req);
}
