package com.green.acamatch.ai;

import com.green.acamatch.ai.model.GetFeedBackRes;
import com.green.acamatch.ai.model.PostFeedBackReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AiMapper {
    int postFeedBack(PostFeedBackReq p);

    List<GetFeedBackRes> getFeedBack(Integer gradeId);
}
