package com.green.acamatch.exam;

import com.green.acamatch.exam.model.ExamPostReq;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ExamMapperTest {

    @Autowired
    private ExamMapper examMapper;

    static final long CLASS_ID_2 = 2L;
    static final long SUBJECT_ID_6 = 6L;
    static final long SUBJECT_ID_8 = 8L;

    static final ExamPostReq existedData = new ExamPostReq();
    static final ExamPostReq notExistedData = new ExamPostReq();

    @BeforeAll
    static void initData() {
        // 기존 데이터 초기화
        existedData.setClassId(CLASS_ID_2);
        existedData.setExamId(SUBJECT_ID_6);
        existedData.setExamName("2회 국어 모의고사");
        existedData.setExamType(1);

        // 존재하지 않는 데이터 초기화
        notExistedData.setClassId(CLASS_ID_2);
        notExistedData.setExamId(SUBJECT_ID_8);
        notExistedData.setExamName("1회 국어 모의고사");
        notExistedData.setExamType(0);
    }

    @Test
    void insExamScore() {
        // 존재하지 않는 과목 삽입
        int result = examMapper.insExamScore(notExistedData);

        // 삽입이 성공했으면 1이 반환되어야 함
        assertAll(
                () -> assertEquals(1, result)
        );
    }

    @Test
    void existsExam() {
        // 존재하는 과목을 조회
        int result = examMapper.existsExam(CLASS_ID_2, "2회 국어 모의고사");

        // 실제 DB에 데이터가 있으면 1, 없으면 0을 반환
        assertTrue(result == 0 || result == 1, "결과는 0 또는 1이어야 합니다.");
    }
}