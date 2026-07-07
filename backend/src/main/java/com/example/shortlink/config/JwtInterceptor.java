package com.example.shortlink.config;

import com.example.shortlink.exception.BusinessException;
import com.example.shortlink.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<Long> USER_HOLDER = new ThreadLocal<>();
    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public static Long currentUserId() {
        Long userId = USER_HOLDER.get();
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(401, "请先登录");
        }
        Long userId = jwtUtil.getUserId(authorization.substring(7));
        if (userId == null) {
            throw new BusinessException(401, "登录状态已失效");
        }
        USER_HOLDER.set(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        USER_HOLDER.remove();
    }
}
