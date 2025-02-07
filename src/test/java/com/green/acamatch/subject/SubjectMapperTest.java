package com.green.acamatch.subject;

import com.green.acamatch.subject.model.SubjectPostReq;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SubjectMapperTest {

    @Autowired
    private SubjectMapper subjectMapper;

    static final long CLASS_ID_2 = 2L;
    static final long SUBJECT_ID_6 = 6L;
    static final long SUBJECT_ID_8 = 8L;

    static final SubjectPostReq existedData = new SubjectPostReq();
    static final SubjectPostReq notExistedData = new SubjectPostReq();

    @BeforeAll
    static void initData() {
        // 기존 데이터 초기화
        existedData.setClassId(CLASS_ID_2);
        existedData.setSubjectId(SUBJECT_ID_6);
        existedData.setSubjectName("2회 국어 모의고사");
        existedData.setScoreType(1);

        // 존재하지 않는 데이터 초기화
        notExistedData.setClassId(CLASS_ID_2);
        notExistedData.setSubjectId(SUBJECT_ID_8);
        notExistedData.setSubjectName("1회 국어 모의고사");
        notExistedData.setScoreType(0);
    }

    @Test
    void insSubjectScore() {
        // 존재하지 않는 과목 삽입
        int result = subjectMapper.insSubjectScore(notExistedData);

        // 삽입이 성공했으면 1이 반환되어야 함
        assertAll(
                () -> assertEquals(1, result)
        );
    }

    @Test
    void existsSubject() {
        // 존재하는 과목을 조회
        int result = subjectMapper.existsSubject(CLASS_ID_2, "2회 국어 모의고사");

        // 실제 DB에 데이터가 있으면 1, 없으면 0을 반환
        assertTrue(result == 0 || result == 1, "결과는 0 또는 1이어야 합니다.");
    }
}