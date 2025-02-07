package com.green.acamatch.academy.Controller;

import com.green.acamatch.academy.Service.CSDService;
import com.green.acamatch.academy.model.HB.GetDongReq;
import com.green.acamatch.academy.model.HB.GetDongRes;
import com.green.acamatch.academy.model.HB.GetStreetReq;
import com.green.acamatch.academy.model.HB.GetStreetRes;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CSDControllerTest {
    @Autowired CSDService service;

    @Test
    void getStreetList() {
        GetStreetReq givenParam = new GetStreetReq();
        givenParam.setCityId(1L);

        assertThrows(DataIntegrityViolationException.class, () -> {
            service.getStreetList(givenParam);
        });
    }

    @Test
    void getDongList() {
        GetDongReq givenParam = new GetDongReq();
        givenParam.setCityId(1L);
        givenParam.setStreetId(10L);

        List<GetDongRes> expectedResult = new ArrayList<>();
        for(GetDongRes res : expectedResult){
            res.setDongId(281L);
            res.setDongName("쌍문동");
        }

        given(service.getDongList(givenParam)).willReturn(expectedResult);

        assertEquals(givenParam.getCityId(), 1L);
        assertEquals(givenParam.getStreetId(), 10L);
    }
}