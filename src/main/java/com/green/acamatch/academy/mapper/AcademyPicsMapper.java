package com.green.acamatch.academy.mapper;

import com.green.acamatch.academy.model.JW.AcademyPicDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AcademyPicsMapper {
    int insAcademyPics(AcademyPicDto academyPicDto);
}
