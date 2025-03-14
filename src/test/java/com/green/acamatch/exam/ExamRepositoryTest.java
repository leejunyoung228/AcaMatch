//package com.green.acamatch.exam;
//
//import com.green.acamatch.config.JpaAuditingConfiguration;
//import com.green.acamatch.entity.acaClass.AcaClass;
//import com.green.acamatch.entity.exam.Exam;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@Import(JpaAuditingConfiguration.class)//created_at, updated_at 현재 일시값 들어갈 수 있도록 autiditing 기능 활성화
//@ActiveProfiles("test") //yaml 적용되는 파일 선택 (application-test.yml)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class ExamRepositoryTest {
//
//@Autowired
//ExamRepository examRepository;
//
//static final Long classId_2 = 2L;
//
//static final Long examId_1 = 1L;
//static final String examName_1 = "학원 모의고사 1차";
//static final int examType_1 = 0;
//
//static final Long examId_3 = 3L;
//static final String examName_3 = "학원 모의고사 3차";
//static final int examType_3 = 0;
//
//Exam existedData = Exam.builder()
//        .examId(Exam.builder().examId(examId_1).build())
//        .classId(AcaClass.builder().classId(classId_2).build());
//
//
//
//
//@BeforeEach
//void setUp() {
//    examRepository.deleteAll();
//    examRepository.save(examRepository);
//}
//
//
//
//
//
//
//
//
//    @Test
//    void existsExam() {
//    }
//}