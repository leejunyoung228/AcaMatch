package com.green.acamatch.teacher;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.config.exception.AcademyException;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.TeacherErrorCode;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.manager.Teacher;
import com.green.acamatch.entity.manager.TeacherIds;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.teacher.model.TeacherDelReq;
import com.green.acamatch.teacher.model.TeacherPostReq;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherMapper teacherMapper;
    private final UserRepository userRepository;
    private final AcademyRepository academyRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public Integer postTeacher(TeacherPostReq p) {
        Teacher teacher = new Teacher();
        Academy academy = academyRepository.findById(p.getAcaId()).orElseThrow(() -> new CustomException(AcademyException.NOT_FOUND_ACADEMY));
        User user = userRepository.findById(p.getUserId()).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        teacher.setAcademy(academy);
        teacher.setUser(user);

        TeacherIds teacherIds = new TeacherIds();
        teacherIds.setAcaId(p.getAcaId());
        teacherIds.setUserId(p.getUserId());

        teacher.setTeacherIds(teacherIds);
        teacher.setTeacherComment(p.getTeacherComment());
        teacher.setTeacherAgree(p.getTeacherAgree());
        teacherRepository.save(teacher);

        return 1;
    }

    @Transactional
    public Integer deleteTeacher(TeacherDelReq p) {
        try {
            Academy academy = academyRepository.findById(p.getAcaId()).orElseThrow(()
                    -> new CustomException(AcademyException.NOT_FOUND_ACADEMY));

            TeacherIds teacherIds = new TeacherIds();
            teacherIds.setAcaId(p.getAcaId());
            teacherIds.setUserId(p.getUserId());

            Teacher teacher = teacherRepository.findById(teacherIds).orElseThrow(()
                    -> new CustomException(TeacherErrorCode.NOT_FOUND_TEACHER));

            if (academy.getAcaId().equals(teacher.getTeacherIds().getAcaId())) {
               teacherRepository.delete(teacher);
                return 1;
            } else {
                throw new IllegalArgumentException("학원에 등록된 선생님이 아닙니다.");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return 0;
        }
    }
}