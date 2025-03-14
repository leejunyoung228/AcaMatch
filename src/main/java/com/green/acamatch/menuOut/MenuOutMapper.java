package com.green.acamatch.menuOut;

import com.green.acamatch.menuOut.model.MenuOutAcademyAllGetRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuOutMapper {
    List<MenuOutAcademyAllGetRes> getAcademyAll();
}
