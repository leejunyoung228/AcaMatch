package com.green.acamatch.acaClass;

import com.green.acamatch.acaClass.model.*;
import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.academyCost.ProductRepository;
import com.green.acamatch.config.exception.*;
import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.acaClass.ClassWeekdays;
import com.green.acamatch.entity.acaClass.ClassWeekdaysIds;
import com.green.acamatch.entity.acaClass.Weekdays;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academyCost.Product;
import com.green.acamatch.entity.category.Category;
import com.green.acamatch.entity.category.ClassCategory;
import com.green.acamatch.entity.category.ClassCategoryIds;
import com.green.acamatch.entity.manager.Teacher;
import com.green.acamatch.entity.manager.TeacherIds;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.joinClass.JoinClassRepository;
import com.green.acamatch.manager.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.Days;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcaClassService {
    private final AcaClassMapper mapper;
    private final UserMessage userMessage;
    private final JoinClassRepository joinClassRepository;
    private final ClassRepository classRepository;
    private final AcademyRepository academyRepository;
    private final ProductRepository productRepository;
    private final WeekDaysRepository weekDaysRepository;
    private final ClassWeekDaysRepository classWeekDaysRepository;
    private final ClassCategoryRepository classCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final TeacherRepository teacherRepository;

    // 특정 학원의 특정 수업을 듣는 학생(또는 학부모) 목록 조회
    public List<User> findStudentsByClassId(Long classId) {
        return joinClassRepository.findStudentsByClassId(classId);
    }

    //수업 등록
    @Transactional
    public int postAcaClass(AcaClassPostReq p) {
        try {
            AcaClass acaClass = new AcaClass();
            Teacher teacher;

            // teacherUserId가 없을 경우 학원 관리자로 설정
            if (p.getTeacherUserId() == null || p.getTeacherUserId() == 0) {
                Academy academy = academyRepository.findById(p.getAcaId())
                        .orElseThrow(() -> new CustomException(AcademyException.NOT_FOUND_ACADEMY));

                Long ownerId = academy.getUser().getUserId(); // 학원 owner 가져오기

                // ownerId를 기반으로 Teacher 객체 생성 또는 조회
                TeacherIds ownerTeacherIds = new TeacherIds();
                ownerTeacherIds.setUserId(ownerId);
                ownerTeacherIds.setAcaId(p.getAcaId());

                teacher = teacherRepository.findByTeacherIds(ownerTeacherIds)
                        .orElseGet(() -> {
                            Teacher newTeacher = new Teacher();
                            newTeacher.setTeacherIds(ownerTeacherIds);
                            newTeacher.setUser(academy.getUser());
                            newTeacher.setAcademy(academy);
                            newTeacher.setTeacherComment("학원 관리자로 자동 설정됨");
                            newTeacher.setTeacherAgree(1);
                            return teacherRepository.save(newTeacher);
                        });
            } else {
                // teacherUserId가 있을 경우 기존 로직 유지
                TeacherIds teacherIds = new TeacherIds();
                teacherIds.setUserId(p.getTeacherUserId());
                teacherIds.setAcaId(p.getAcaId());

                teacher = teacherRepository.findByTeacherIds(teacherIds)
                        .orElseThrow(() -> new CustomException(ManagerErrorCode.TEACHER_NOT_FOUND));
            }

            // teacherUserId 값 보정
            Long teacherUserId = Optional.ofNullable(p.getTeacherUserId()).orElse(0L);

            // 중복 강좌 검사 (운영 시간(start_time, end_time) 제외)
            Long duplicateCount = classRepository.countByAcaIdAndClassNameAndStartDateAndEndDateAndTeacherUserId(
                    p.getAcaId(), p.getClassName(), p.getStartDate(), p.getEndDate(), teacherUserId);

            if (duplicateCount > 0) {
                throw new CustomException(ManagerErrorCode.CLASS_ALREADY_EXISTS);
            }

            // 학원 정보 설정
            Academy academy = academyRepository.findById(p.getAcaId())
                    .orElseThrow(() -> new CustomException(AcademyException.NOT_FOUND_ACADEMY));
            acaClass.setAcademy(academy);
            acaClass.setClassName(p.getClassName());
            acaClass.setClassComment(p.getClassComment());
            acaClass.setStartDate(p.getStartDate());
            acaClass.setEndDate(p.getEndDate());
            acaClass.setStartTime(p.getStartTime());
            acaClass.setEndTime(p.getEndTime());
            acaClass.setPrice(p.getPrice());
            acaClass.setTeacher(teacher); // Teacher 설정

            // 강좌 저장
            AcaClass savedClass = classRepository.save(acaClass);

            // 중복된 Product 생성 방지
            boolean productExists = productRepository.existsByClassId(savedClass);

            if (!productExists) {
                Product product = new Product();
                product.setClassId(savedClass);
                product.setProductName(p.getClassName()); // 강좌 이름을 상품명으로 설정
                product.setProductPrice(p.getPrice());
                productRepository.save(product);
            }

            return 1;
        } catch (CustomException e) {
            e.printStackTrace();
            return 0;
        }
    }




    //요일 등록
    @Transactional
    public int insWeekDay(WeekDays p) {
        try {
            // 이미 존재하는 요일인지 확인
            if (weekDaysRepository.existsDay(p.getDay()) > 0) {
                throw new IllegalArgumentException("이미 존재하는 요일입니다.");
            }

            // 존재하지 않으면 새로 등록
            Weekdays weekdays = new Weekdays();
            weekdays.setDay(p.getDay());
            weekDaysRepository.save(weekdays);
            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }

    //개강날 등록
    @Transactional
    public int insAcaClassClassWeekDays(ClassWeekDaysReq p) {
        try {
            // 강좌가 존재하는지 확인
            AcaClass acaClass = classRepository.findById(p.getClassId())
                    .orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_CLASS));

            // 요일이 존재하는지 확인
            Weekdays weekdays = weekDaysRepository.findById(p.getDayId())
                    .orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_DAY));

            // 중복된 강좌 요일이 있는지 확인
            if (classWeekDaysRepository.existsClassWeekDays(p.getDayId(), p.getClassId()) > 0) {
                throw new IllegalArgumentException("중복된 강좌 요일입니다.");
            }

            // ClassWeekdaysIds 생성 및 classId, dayId 설정
            ClassWeekdaysIds classWeekdaysIds = new ClassWeekdaysIds();
            classWeekdaysIds.setClassId(acaClass.getClassId());  // AcaClass의 ID 값을 설정
            classWeekdaysIds.setDayId(weekdays.getDayId());    // Weekdays의 ID 값을 설정

            // 새 ClassWeekdays 객체 생성 및 저장
            ClassWeekdays classWeekDays = new ClassWeekdays();

            classWeekDays.setClassWeekdaysIds(classWeekdaysIds);
            classWeekDays.setClassId(acaClass);
            classWeekDays.setDay(weekdays);
            classWeekDaysRepository.save(classWeekDays);

            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }

    //카테고리 등록
    @Transactional
    public int insAcaClassCategory(AcaClassCategoryReq p) {
        try {
            ClassCategory classCategory = new ClassCategory();
            classCategory.setClassId(classRepository.findById(p.getClassId()).orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_CLASS)));
            Category category = categoryRepository.findById(p.getCategoryId())
                    .orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_CATEGORY));

            if (classCategoryRepository.existsCategory(p.getClassId(), p.getCategoryId()) > 0) {
                throw new IllegalArgumentException("중복된 카테고리입니다.");
            }

            AcaClass acaClass = new AcaClass();
            ClassCategoryIds classCategoryIds = new ClassCategoryIds();
            classCategoryIds.setCategoryId(category.getCategoryId());
            classCategoryIds.setClassId(acaClass.getClassId());

            classCategory.setClassCategoryIds(classCategoryIds);
            classCategory.setCategoryId(category);
            classCategoryRepository.save(classCategory);

            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }

    //수업 상세정보 불러오기
    public List<AcaClassDetailDto> getClass(AcaClassDetailGetReq p) {
        try {
            List<AcaClassDetailDto> result = mapper.selAcaClassDetail(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("상세정보 불러오기에 실패하였습니다.");
                return null;
            }
            userMessage.setMessage("상세정보 불러오기에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("기타 오류 사항으로 상세정보를 불러오지 못했습니다.");
            return null;
        }
    }

    //특정 user가 등록한 class 가져오기
    public List<AcaClassUserDto> getClassUser(AcaClassUserGetReq p) {
        try {
            List<AcaClassUserDto> result = mapper.selAcaClassUser(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("등록한 강좌 정보 불러오기에 실패하였습니다.");
                return null;
            }
            userMessage.setMessage("등록한 강좌 정보 불러오기에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("기타 오류 사항으로 정보를 불러오지 못했습니다.");
            return null;
        }
    }

    //학원 강좌 가져오기
    public List<AcaClassDto> selAcaClass(AcaClassGetReq p) {
        try {
            List<AcaClassDto> result = mapper.selAcaClass(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("학원 강좌 정보 불러오기에 실패하였습니다.");
                return null;
            }
            userMessage.setMessage("학원 강좌 정보 불러오기에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("기타 오류 사항으로 정보를 불러오지 못했습니다.");
            return null;
        }
    }

    //강좌 수정
    @Transactional
    public int updAcaClass(AcaClassPutReq p) {
        try {
            Academy academy = academyRepository.findById(p.getAcaId()).orElseThrow(() -> new CustomException(AcademyException.NOT_FOUND_ACADEMY));
            AcaClass acaClass = classRepository.findById(p.getClassId()).orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_CLASS));
            acaClass.setAcademy(academy);
            acaClass.setClassId(acaClass.getClassId());
            if (p.getClassName() != null && p.getClassName().isEmpty()) {
                throw new CustomException(AcaClassErrorCode.FAIL_TO_UPD);
            }
            if (p.getClassComment() != null && p.getClassComment().isEmpty()) {
                throw new CustomException(AcaClassErrorCode.FAIL_TO_UPD);
            }
            if (p.getPrice() < 0) {
                throw new CustomException(AcaClassErrorCode.FAIL_TO_UPD);
            }

            acaClass.setClassName(p.getClassName());
            acaClass.setClassComment(p.getClassComment());
            acaClass.setPrice(p.getPrice());
            acaClass.setStartDate(p.getStartDate());
            acaClass.setEndDate(p.getEndDate());
            acaClass.setStartTime(p.getStartTime());
            acaClass.setEndTime(p.getEndTime());

            classRepository.save(acaClass);
            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }

//        try {
//            int result = mapper.updAcaClass(p);
//            userMessage.setMessage("강좌 정보 수정에 성공하였습니다.");
//            return result;
//        } catch (BadSqlGrammarException e) {
//            userMessage.setMessage("잘못된 형식을 입력하였습니다.");
//            return 0;
//        }
    }

    //class 삭제
    public int delAcaClass(AcaClassDelReq p) {
        try {
            Academy academy = academyRepository.findById(p.getAcaId()).orElseThrow(() -> new CustomException(AcademyException.NOT_FOUND_ACADEMY));
            AcaClass acaClass = classRepository.findById(p.getClassId()).orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_CLASS));

            if (academy.getAcaId().equals(acaClass.getAcademy().getAcaId())) {
                classRepository.delete(acaClass);
                return 1;
            } else {
                throw new CustomException(AcaClassErrorCode.INVALID_DAY_FOR_CLASS);
            }
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
//        int result = mapper.delAcaClass(p);
//
//        if (result == 1) {
//            userMessage.setMessage("강좌 삭제에 성공하였습니다.");
//            return result;
//        } else {
//            userMessage.setMessage("강좌 삭제에 실패하였습니다.");
//            return 0;
//        }
    }

    // 수업이 열리는 요일 삭제하기
    public int delAcaClassDay(ClassWeekDaysReq p) {
        try {
            AcaClass acaClass = classRepository.findById(p.getClassId()).orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_CLASS));
            Weekdays weekdays = weekDaysRepository.findById(p.getDayId()).orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_DAY));
            // 특정 강좌에 속한 요일만 삭제
            if (weekdays.getClass().equals(acaClass)) {
                weekDaysRepository.delete(weekdays);
                return 1;
            } else {
                throw new CustomException(AcaClassErrorCode.INVALID_DAY_FOR_CLASS);
            }
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
//        int result = mapper.delAcaClassDay(p);
//
//        if (result == 1) {
//            userMessage.setMessage("개강날 삭제에 성공하였습니다.");
//            return result;
//        } else {
//            userMessage.setMessage("개강날 삭제에 실패하였습니다.");
//            return 0;
//        }
    }

    /**
     * 특정 수업의 담당 선생님인지 확인
     */
    public boolean isTeacherOfClass(long userId, long classId) {
        Teacher teacher = classRepository.findTeacherByClassId(classId);
        return teacher != null && teacher.getTeacherIds().getUserId().equals(userId);
    }

    /**
     * 특정 수업의 담당 선생님인지 검증 (권한 없을 경우 예외 발생)
     */
    public void validateTeacherPermission(long userId, long classId) {
        if (!isTeacherOfClass(userId, classId)) {
            throw new CustomException(ManagerErrorCode.PERMISSION_DENIED);
        }
    }
}