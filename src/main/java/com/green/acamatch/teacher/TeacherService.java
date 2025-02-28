package com.green.acamatch.teacher;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.UserConst;
import com.green.acamatch.config.exception.*;
import com.green.acamatch.config.security.AuthenticationFacade;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserUtils userUtils;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MyFileUtils myFileUtils;
    private final UserConst userConst;
    @Autowired
    private JavaMailSender mailSender;

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

//    @Transactional
//    public int updateTeacher(TeacherPutReq p, MultipartFile pic, String inputCode) {
//        Long userId = AuthenticationFacade.getSignedUserId();
//
//        // 이메일 인증 코드 검증
//        if (!verifyCode(userId, inputCode)) {
//            throw new CustomException(UserErrorCode.INVALID_VERIFICATION_CODE);
//        }
//
//        // 사용자 정보 조회
//        User user = userUtils.findUserById(userId);
//        Teacher teacher = teacherRepository.findByAcaIdAndUserId(userId)
//                .orElseThrow(() -> new CustomException(UserErrorCode.TEACHER_NOT_FOUND));
//
//        String email = teacherRepository.findEmailByUserId(AuthenticationFacade.getSignedUserId());
//        if(p.getEmail() != null) user.setEmail(email);
//        if (p.getCurrentPw() == null || !passwordEncoder.matches(p.getCurrentPw(), user.getUpw())) {
//            throw new CustomException(UserErrorCode.INCORRECT_PW);
//        }
//        if (p.getNewPw() != null) user.setUpw(passwordEncoder.encode(p.getNewPw()));
//        if (p.getName() != null) user.setName(p.getName());
//        if (p.getNickName() != null) user.setNickName(p.getNickName());
//        if (p.getPhone() != null) user.setPhone(p.getPhone());
//        if (p.getBirth() != null) user.setBirth(p.getBirth());
//        if (pic != null && !pic.isEmpty()) updateTeacherProfile(user, pic);
//        if (p.getTeacherComment() != null) teacher.setTeacherComment(p.getTeacherComment());
//
//        userRepository.save(user);
//        teacherRepository.save(teacher);
//
//        return 1;
//    }
//
//    public void updateTeacherProfile(User user, MultipartFile pic) {
//        String prePic = user.getUserPic();
//        String folderPath = String.format(userConst.getUserPicFilePath(), user, user.getUserId());
//        user.setUserPic(myFileUtils.makeRandomFileName(pic));
//        String filePath = String.format("%s/%s", folderPath, user.getUserPic());
//        if (prePic != null) {
//            myFileUtils.deleteFolder(folderPath, false);
//        }
//        myFileUtils.makeFolders(folderPath);
//        try {
//            myFileUtils.transferTo(pic, filePath);
//        } catch (IOException e) {
//            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public String generateVerificationCode() {
//        return UUID.randomUUID().toString().substring(0, 6); // 6자리 랜덤 코드
//    }
//
//    public void sendVerificationEmail(String email, String code) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("본인 인증 코드");
//        message.setText("인증 코드: " + code);
//        mailSender.send(message);
//    }
//
//    // 인증 코드 저장소 (임시: Redis 또는 DB 사용 가능)
//    private Map<Long, String> verificationCodes = new HashMap<>();
//
//    public void requestEmailVerification(Long userId) {
//        String email = teacherRepository.findEmailByUserId(userId);
//        String code = generateVerificationCode();
//        verificationCodes.put(userId, code);
//        sendVerificationEmail(email, code);
//    }
//
//    // 사용자가 입력한 인증 코드 검증
//    public boolean verifyCode(Long userId, String inputCode) {
//        String correctCode = verificationCodes.get(userId);
//        return correctCode != null && correctCode.equals(inputCode);
//    }
}