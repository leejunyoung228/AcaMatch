package com.green.acamatch.user.service;

import com.green.acamatch.config.CodeGenerate;
import com.green.acamatch.config.CookieUtils;
import com.green.acamatch.config.constant.EmailConst;
import com.green.acamatch.config.constant.JwtConst;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.jwt.JwtTokenProvider;
import com.green.acamatch.config.jwt.JwtUser;
import com.green.acamatch.entity.reports.Reports;
import com.green.acamatch.reports.ReportsMapper;
import com.green.acamatch.reports.ReportsRepository;
import com.green.acamatch.reports.model.GetReportDateRes;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.model.FindPwReq;
import com.green.acamatch.user.model.UserSignInReq;
import com.green.acamatch.user.model.UserSignInRes;
import com.green.acamatch.user.model.UserSignUpReq;
import com.green.acamatch.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class AuthService {
    private final EmailService emailService;
    private final UserCache userCache;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtConst jwtConst;
    private final EmailConst emailConst;
    private final CookieUtils cookieUtils;
    private final UserUtils userUtils;
    private final ReportsRepository reportsRepository;
    private final UserRepository userRepository;
    private final ReportsMapper reportsMapper;

    public UserSignInRes signIn(UserSignInReq req, HttpServletResponse response) {
        User user = userUtils.findUserByEmail(req.getEmail());

        if (!passwordEncoder.matches(req.getUpw(), user.getUpw())) {
            throw new CustomException(UserErrorCode.INCORRECT_ID_PW);
        }

        GetReportDateRes result = reportsMapper.getReportDate(req.getEmail());
        if(result == null){
            return userUtils.generateUserSignInResByUser(user, response);
        }
        if (result.getExposureEndDate() != null) {
            // 날짜 포맷 정의 (예: "yyyy-MM-dd HH:mm:ss")
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // String → LocalDateTime 변환
            LocalDateTime exposureEndDate = LocalDateTime.parse(result.getExposureEndDate(), formatter);

            // 현재 날짜/시간 가져오기
            LocalDateTime now = LocalDateTime.now();

            // 날짜 비교: exposureEndDate가 현재 시간 이후면 예외 발생
            if (exposureEndDate.isAfter(now)) {
                throw new CustomException(UserErrorCode.BAN_USER);
            }
        }

        return userUtils.generateUserSignInResByUser(user, response);
    }


    public int sendSignUpEmail(UserSignUpReq req) {
        userUtils.checkDuplicate(req.getEmail(), "email");
        userUtils.checkDuplicate(req.getNickName(), "nick-name");

        User user = userUtils.generateUserByUserSignUpReq(req);
        String token = UUID.randomUUID().toString();
        emailService.sendCodeToEmail(
                req.getEmail(),
                emailConst.getSignUpSubject(),
                emailService.getHtmlTemplate(emailConst.getSignUpTemplateName(),
                        emailService.getContext(emailConst.getSignUpUrl(), emailConst.getTokenKey(), token))
        );
        userCache.saveToken(token, user);
        return 1;
    }

    public int sendTempPwEmail(FindPwReq req) {
        User user = userUtils.findUserByEmail(req.getEmail());
        String pw = CodeGenerate.generateCode(8);
        userCache.saveTempPw(user.getUserId(), pw);
        emailService.sendCodeToEmail(
                req.getEmail(),
                emailConst.getFindPwSubject(),
                emailService.getHtmlTemplate(emailConst.getFindPwTemplateName(),
                        emailService.getContext(emailConst.getTempPwUrl(), user.getUserId(), pw))
        );
        return 1;
    }

    public String getAccessToken(HttpServletRequest request) {
        Cookie cookie = cookieUtils.getCookie(request, jwtConst.getRefreshTokenCookieName());
        String refreshToken = cookie.getValue();
        JwtUser jwtUser = jwtTokenProvider.getJwtUserFromToken(refreshToken);
        return jwtTokenProvider.generateAccessToken(jwtUser);
    }

    public int logOutUser(HttpServletResponse response) {
        cookieUtils.deleteCookieToken(response, jwtConst.getRefreshTokenCookieName());
        return 1;
    }


}
