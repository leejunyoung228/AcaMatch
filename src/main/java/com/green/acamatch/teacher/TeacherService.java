package com.green.acamatch.teacher;

import com.green.acamatch.acaClass.ClassRepository;
import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.UserConst;
import com.green.acamatch.config.exception.*;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.manager.Teacher;
import com.green.acamatch.entity.manager.TeacherIds;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.teacher.model.TeacherDelReq;
import com.green.acamatch.teacher.model.TeacherPostReq;
import com.green.acamatch.teacher.model.TeacherPutReq;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherMapper teacherMapper;
    private final UserRepository userRepository;
    private final AcademyRepository academyRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ClassRepository classRepository;

    @Transactional
    public Integer postTeacher(TeacherPostReq p) {
        if(teacherRepository.existsByAcademyAndClassAndUser(p.getAcaId(), p.getClassId(), p.getUserId()) > 0) {
            throw new CustomException(AcaClassErrorCode.EXISTS_TEACHER);
        }
        Teacher teacher = new Teacher();
        AcaClass acaClass = classRepository.findById(p.getClassId()).orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_CLASS));
        User user = userRepository.findById(p.getUserId()).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        teacher.setAcaClass(acaClass);
        teacher.setUser(user);

        TeacherIds teacherIds = new TeacherIds();
        teacherIds.setClassId(p.getClassId());
        teacherIds.setUserId(p.getUserId());

        teacher.setTeacherIds(teacherIds);
        teacher.setTeacherComment(p.getTeacherComment());
        teacher.setTeacherAgree(p.getTeacherAgree());
        teacherRepository.save(teacher);

        return 1;
    }

    public Integer updateTeacher(TeacherPutReq p) {
        try {
            TeacherIds teacherIds = new TeacherIds(p.getClassId(), p.getUserId());
            teacherIds.setClassId(p.getClassId());
            teacherIds.setUserId(p.getUserId());

            Teacher teacher = teacherRepository.findById(teacherIds).orElseThrow(()
                    -> new CustomException(TeacherErrorCode.NOT_FOUND_TEACHER));

            if (!StringUtils.hasText(p.getTeacherComment())) {
                throw new CustomException(AcaClassErrorCode.FAIL_TO_UPD);
            }

            if(p.getTeacherAgree() < 0) {
                throw new CustomException(AcaClassErrorCode.FAIL_TO_UPD);
            }

            teacher.setTeacherComment(p.getTeacherComment());
            teacher.setTeacherAgree(p.getTeacherAgree());
            teacherRepository.save(teacher);

            return 1;
        } catch (CustomException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    public Integer deleteTeacher(TeacherDelReq p) {
        try {
            AcaClass acaClass = classRepository.findById(p.getClassId()).orElseThrow(()
                    -> new CustomException(AcaClassErrorCode.NOT_FOUND_CLASS));

            TeacherIds teacherIds = new TeacherIds();
            teacherIds.setClassId(p.getClassId());
            teacherIds.setUserId(p.getUserId());

            Teacher teacher = teacherRepository.findById(teacherIds).orElseThrow(()
                    -> new CustomException(TeacherErrorCode.NOT_FOUND_TEACHER));

            if (acaClass.getClassId().equals(teacher.getTeacherIds().getClassId())) {
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