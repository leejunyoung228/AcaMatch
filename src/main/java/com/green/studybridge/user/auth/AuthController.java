package com.green.studybridge.user.auth;

import com.green.studybridge.user.UserService;
import com.green.studybridge.user.model.UserSignInRes;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping
    public String finishSignUp(@RequestParam("token") String token, HttpServletResponse response) {
        userService.signUp(token, response);
        return "redirect:/index.html";
    }
}
