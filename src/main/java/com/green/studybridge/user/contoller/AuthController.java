package com.green.studybridge.user.contoller;

import com.green.studybridge.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping
    public void finishSignUp(@RequestParam("token") String token, HttpServletResponse response, HttpSession session) {
        userService.signUp(token, response, session);
    }
}
