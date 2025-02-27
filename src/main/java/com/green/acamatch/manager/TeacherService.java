package com.green.acamatch.manager;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.config.exception.*;
import com.green.acamatch.config.security.AuthenticationFacade;
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
    private final UserMessage userMessage;

    /**
     * 선생님 서비스에서 로그인된 사용자 검증
     */
    private long validateAuthenticatedUser(long requestUserId) {
        long jwtUserId = AuthenticationFacade.getSignedUserId(); // 로그인된 사용자 아이디 조회

        // 사용자 존재 여부 체크 추가
        validateUserExists(jwtUserId);

        if (jwtUserId != requestUserId) {
            // CustomException에 상세 메시지를 포함하여 던짐
            throw new CustomException(ReviewErrorCode.UNAUTHENTICATED_USER);
        }
        return jwtUserId;
    }

    /**
     * JWT userId와 요청 userId 비교
     */
    private boolean isAuthorizedUser(long requestUserId) {
        long jwtUserId = AuthenticationFacade.getSignedUserId(); // 로그인된 사용자 아이디 조회

        if (jwtUserId != requestUserId) {
            String errorMessage = String.format("로그인한 유저의 아이디(%d)와 요청한 유저의 아이디(%d)가 일치하지 않습니다.", jwtUserId, requestUserId);
            userMessage.setMessage(errorMessage);
            return false;
        }
        return true;
    }

    /**
     * 사용자 ID가 DB에 존재하는지 확인
     */
    private void validateUserExists ( long userId){
        if (!userRepository.existsByUserId(userId)) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        }
    }


    @Transactional
    public void registerTeacher(TeacherRegisterReq req) {

        long jwtUserId = validateAuthenticatedUser(req.getUserId());
        long requestUserId = req.getUserId();


        // 본인 계정 검증
        if (jwtUserId != requestUserId) {
            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 선생님으로 등록할 수 있습니다.");
        }

        // 유저 존재 여부 확인
        validateUserExists(requestUserId);

        if (!isAuthorizedUser(req.getUserId())) {
            throw new CustomException(UserErrorCode.UNAUTHENTICATED);
        }

        if (academyRepository.checkAcaExists(req.getAcaId()) == 0) {
            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
        }

        if (!userRepository.existsByUserId(req.getUserId())) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
        }

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
