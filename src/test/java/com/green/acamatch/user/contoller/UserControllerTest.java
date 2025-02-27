package com.green.acamatch.user.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.config.constant.JwtConst;
import com.green.acamatch.config.jwt.JwtTokenProvider;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.user.model.*;
import com.green.acamatch.user.service.AuthService;
import com.green.acamatch.user.service.UserManagementService;
import com.green.acamatch.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JwtTokenProvider.class})
@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@EnableConfigurationProperties(JwtConst.class)
class UserControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;
    @MockitoBean
    AuthService authService;
    @MockitoBean
    UserManagementService userManagementService;
    @MockitoBean
    UserUtils userUtils;

    final String BASE_URL = "/api/user";

    final long USER_ID_1 = 1L;
    final long ROLE_ID_1 = 1L;
    final String USER_NAME_1 = "user1";
    final String EMAIL_1 = "user1@gmail.com";
    final String PHONE_1 = "010-1111-1111";
    final LocalDate BIRTH_1 = LocalDate.of(1990, 1, 1);
//    final UserRole = "ADMIN";
    final String NICK_NAME_1 = "userNickName1";
    final String USER_PIC_1 = "userPic1.jpg";
    final String PASSWORD_1 = "Test1234@";
    final LocalDateTime CREATE_AT_1 = LocalDateTime.of(2024, 1, 1, 1, 1);
    final LocalDateTime UPDATE_AT_1 = LocalDateTime.of(2025, 1, 1, 1, 1);
    final String ACCESS_TOKEN_1 = "access_token1";

    // 1. 로그인 테스트
    @Test
    void signIn() throws Exception {
        UserSignInReq givenParam = new UserSignInReq(EMAIL_1, PASSWORD_1);
        UserSignInRes expectedResult = UserSignInRes.builder()
                .userId(USER_ID_1)
//                .userRole("user1")
                .name(USER_NAME_1)
                .accessToken(ACCESS_TOKEN_1)
                .build();
        given(authService.signIn(eq(givenParam), any(HttpServletResponse.class))).willReturn(expectedResult);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/sign-in")
                .content(objectMapper.writeValueAsString(givenParam))
                .contentType(MediaType.APPLICATION_JSON));

        String expectedResJson = objectMapper.writeValueAsString(
                ResultResponse.<UserSignInRes>builder()
                        .resultMessage("로그인 성공")
                        .resultData(expectedResult)
                        .build()
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(authService).signIn(eq(givenParam), any(HttpServletResponse.class));
    }

    // 2. 회원가입 완료 테스트 (프론트에서 사용 안함)
    @Test
    void finishSignUp() throws Exception {
        String token = "testToken";
        mockMvc.perform(get(BASE_URL + "/sign-up")
                        .param("token", token))
                .andExpect(status().isOk());

        verify(userManagementService).signUp(eq(token), any(HttpServletResponse.class));
    }

    // 3. 중복 체크 테스트
    @Test
    void checkDuplicate() throws Exception {
        String text = EMAIL_1;
        String type = "email";
        given(userUtils.checkDuplicate(text, type)).willReturn(0);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/check-duplicate/{type}", type)
                .param("text", text));

        String expectedResJson = objectMapper.writeValueAsString(
                ResultResponse.<Integer>builder()
                        .resultMessage("중복 되지 않습니다")
                        .resultData(0)
                        .build()
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(userUtils).checkDuplicate(eq(text), eq(type));
    }


    // 5. 임시 비밀번호 요청 테스트
    @Test
    void tempPwRequest() throws Exception {
        FindPwReq givenParam = new FindPwReq();
        givenParam.setEmail(EMAIL_1);
        given(authService.sendTempPwEmail(givenParam)).willReturn(1);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/temp-pw")
                .content(objectMapper.writeValueAsString(givenParam))
                .contentType(MediaType.APPLICATION_JSON));

        String expectedResJson = objectMapper.writeValueAsString(
                ResultResponse.<Integer>builder()
                        .resultMessage("임시 비밀번호 전송 성공")
                        .resultData(1)
                        .build()
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(authService).sendTempPwEmail(givenParam);
    }

    // 6. 로그아웃 테스트
    @Test
    void logout() throws Exception {
        given(authService.logOutUser(any(HttpServletResponse.class))).willReturn(1);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/log-out"));

        String expectedResJson = objectMapper.writeValueAsString(
                ResultResponse.<Integer>builder()
                        .resultMessage("로그아웃 성공")
                        .resultData(1)
                        .build()
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(authService).logOutUser(any(HttpServletResponse.class));
    }

    // 7. 액세스 토큰 재발행 테스트
    @Test
    void getAccessToken() throws Exception {
        given(authService.getAccessToken(any(HttpServletRequest.class))).willReturn("newAccessToken");

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/access-token"));

        String expectedResJson = objectMapper.writeValueAsString(
                ResultResponse.<String>builder()
                        .resultMessage("액세스 토큰 재발행 성공")
                        .resultData("newAccessToken")
                        .build()
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(authService).getAccessToken(any(HttpServletRequest.class));
    }

    // 8. 유저 수 조회 테스트
    @Test
    void getTotalUser() throws Exception {
        given(userService.getTotalUserCount()).willReturn(100L);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/total_user"));

        String expectedResJson = objectMapper.writeValueAsString(
                ResultResponse.<Long>builder()
                        .resultMessage("유저수 조회 성공")
                        .resultData(100L)
                        .build()
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(userService).getTotalUserCount();
    }
}
