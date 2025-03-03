package com.green.acamatch.popUp;

import com.green.acamatch.popUp.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PopUpMapper {
//    int PostPopUp(PopUpPostReq p);
    List<PopUpGetDto> getPopUpList(PopUpGetReq p);
    List<PopUpGetDto> getPopUpDetail(PopUpGetDetailReq p);
}