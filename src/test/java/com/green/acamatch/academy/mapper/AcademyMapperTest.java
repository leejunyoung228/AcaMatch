package com.green.acamatch.academy.mapper;

import com.green.acamatch.academy.model.HB.GetAcademyCountRes;
import com.green.acamatch.academy.model.HB.PostAcademySearch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AcademyMapperTest {
    @Mock AcademyMapper academyMapper;

    @Test
    void postToSearch() {
        final int EXPECTED_RESULT = 1;
        final int EXPECTED_TAG_ID = 1;

        PostAcademySearch givenParam = new PostAcademySearch();
        givenParam.setTagId(EXPECTED_TAG_ID);
        given(academyMapper.postSearch(givenParam)).willReturn(EXPECTED_RESULT);

        //when
        PostAcademySearch actualParam = new PostAcademySearch();
        actualParam.setTagId(EXPECTED_TAG_ID); // tagId 설정
        int actualResult = academyMapper.postSearch(actualParam);

        //then
        assertEquals(EXPECTED_RESULT, actualResult);
    }

    @Test
    void GetAcademyCount() {
        GetAcademyCountRes res = new GetAcademyCountRes();
        res.setAcademyCount(102);
        res.setUserCount(205);

        given(academyMapper.GetAcademyCount()).willReturn(res);

        GetAcademyCountRes actualRes = new GetAcademyCountRes();
        actualRes.setAcademyCount(102);
        actualRes.setUserCount(205);

        GetAcademyCountRes actualResult = academyMapper.GetAcademyCount();

        assertEquals(res, actualResult);
    }
}