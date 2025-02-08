package com.green.acamatch.ai;


import com.green.acamatch.academy.model.AddressDto;
import com.green.acamatch.ai.model.GetFeedBackRes;
import com.green.acamatch.ai.model.PostFeedBackReq;
import com.green.acamatch.config.constant.ApiConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {
    private final AiMapper mapper;
    private final ApiConst apiConst;

    public int postFeedBack(PostFeedBackReq p){
        if(p.getGradeId() == null){
            p.setMessage("잘못된 gradeId입니다.");
            return 0;
        }

        else if(p.getFeedBack() == null){
            p.setMessage("피드백이 없습니다.");
            return 0;
        }

        p.setMessage("피드백 등록 성공");
        return mapper.postFeedBack(p);
    }

    public List<GetFeedBackRes> getFeedBack(Integer gradeId){
        List<GetFeedBackRes> res = mapper.getFeedBack(gradeId);
        return res;
    }

    public String getApiKey() {
        return apiConst.getApiKey();
    }
}
