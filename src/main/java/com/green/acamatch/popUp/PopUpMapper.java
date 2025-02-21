package com.green.acamatch.popUp;

import com.green.acamatch.popUp.model.PopUpPostReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PopUpMapper {
    int PostPopUp(PopUpPostReq p);
}