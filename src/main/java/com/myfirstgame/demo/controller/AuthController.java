package com.myfirstgame.demo.controller;

import com.myfirstgame.demo.service.AuthService;
import com.myfirstgame.demo.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return authService.logout();
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(@CookieValue(value = "jwt", defaultValue = "") String token) {
        return authService.checkAuth(token); // 서비스 계층 호출
    }
}
