package com.green.acamatch.manager;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.ManagerErrorCode;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.manager.Teacher;
import com.green.acamatch.entity.manager.TeacherIds;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.manager.model.TeacherRegisterReq;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final AcademyRepository academyRepository;

    @Transactional
    public void registerTeacher(TeacherRegisterReq req) {
        // 학원 조회
        Academy academy = academyRepository.findById(req.getAcaId())
                .orElseThrow(() -> new CustomException(ManagerErrorCode.ACADEMY_NOT_FOUND));

        // 사용자 조회 (강사)
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new CustomException(ManagerErrorCode.TEACHER_NOT_FOUND));

        // TeacherIds 생성 (복합키)
        TeacherIds teacherIds = new TeacherIds();
        teacherIds.setUserId(req.getUserId());
        teacherIds.setAcaId(req.getAcaId());

        // 이미 등록된 선생님인지 확인
        if (teacherRepository.findByTeacherIds(teacherIds).isPresent()) {
            throw new CustomException(ManagerErrorCode.TEACHER_ALREADY_EXISTS);
        }

        // Teacher 엔티티 생성 후 저장
        Teacher teacher = new Teacher();
        teacher.setTeacherIds(teacherIds);
        teacher.setUser(user);
        teacher.setAcademy(academy);
        teacher.setTeacherComment(req.getTeacherComment());
        teacher.setTeacherAgree(req.getTeacherAgree());

        teacherRepository.save(teacher);
    }
}
