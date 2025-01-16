package com.myfirstgame.demo.interceptor;

import com.myfirstgame.demo.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * preHandle :
 * 컨트롤러에 요청이 전달되기 전에 실행됩니다.
 * JWT 검증, 인증 및 요청의 유효성을 확인합니다.
 * 인증 실패 시 요청을 차단하고 401 Unauthorized 응답을 반환합니다.
 *
 * postHandle:
 * 컨트롤러에서 요청을 처리한 뒤에 실행됩니다.
 * 요청 처리 결과를 수정하거나 로그를 기록하는 데 사용됩니다.
 *
 * afterCompletion:
 * 요청 처리 완료 후(응답을 클라이언트로 보낸 후) 실행됩니다.
 * 리소스 정리 작업 등에 사용됩니다.
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = null;

        // 쿠키에서 JWT 추출
        if(request.getCookies() != null) {
            for (Cookie cookie: request.getCookies()) {
                if("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if(token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            // JWTUtils에서 토큰 검증 및 사용자 정보 추출
            String username = JwtUtils.validateToken(token);
            request.setAttribute("username", username);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;   // 요청 차단
        }

        return true;    // 요청 성공
    }
}
