package com.green.acamatch.user.contoller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.config.constant.JwtConst;
import com.green.acamatch.config.jwt.JwtTokenProvider;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.user.model.UserInfo;
import com.green.acamatch.user.service.AuthService;
import com.green.acamatch.user.service.UserManagementService;
import com.green.acamatch.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JwtTokenProvider.class})
@WebMvcTest(
        controllers = UserController.class
        , excludeAutoConfiguration = SecurityAutoConfiguration.class
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
    final String NICK_NAME_1 = "userNickName1";
    final String USER_PIC_1 = "userPic1.jpg";
    final LocalDateTime CREATE_AT_1 = LocalDateTime.of(2024, 1, 1, 1, 1);
    final LocalDateTime UPDATE_AT_1 = LocalDateTime.of(2025, 1, 1, 1, 1);

    @Test
    void getUser() throws Exception {
        UserInfo expectedResult = UserInfo.builder()
                .userId(USER_ID_1)
                .roleId(ROLE_ID_1)
                .name(USER_NAME_1)
                .email(EMAIL_1)
                .phone(PHONE_1)
                .birth(BIRTH_1)
                .nickName(NICK_NAME_1)
                .userPic(USER_PIC_1)
                .createdAt(CREATE_AT_1)
                .updatedAt(UPDATE_AT_1)
                .build();
        given(userService.getUserInfo()).willReturn(expectedResult);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL));

        String expectedResJson = getExpectedResJson(expectedResult);

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(userService).getUserInfo();
    }

    private String getExpectedResJson(UserInfo expectedResult) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ResultResponse.<UserInfo> builder()
                        .resultMessage("회원 정보 조회 성공")
                        .resultData(expectedResult)
                        .build()
        );
    }

}