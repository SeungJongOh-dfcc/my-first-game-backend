package com.myfirstgame.demo.service;

import com.myfirstgame.demo.repository.UserRepository;
import com.myfirstgame.demo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    public ResponseEntity<?> logout() {
        // 쿠키 만료 설정
        ResponseCookie cookie = ResponseCookie.from(("jwt"), "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body("Logout successful");
    }

    public ResponseEntity<?> checkAuth(String token) {
        if (token.isEmpty()) {
            return ResponseEntity.ok(Map.of("isAuthenticated", false));
        }

        try {
            String username = JwtUtils.validateToken(token); // JWT 검증
            return ResponseEntity.ok(Map.of(
                    "isAuthenticated", true,
                    "username", username
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("isAuthenticated", false));
        }
    }
}
