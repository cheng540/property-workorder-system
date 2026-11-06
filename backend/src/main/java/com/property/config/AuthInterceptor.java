package com.property.config;

import com.property.util.JwtUtil;
import com.property.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.annotation.Resource;
import java.io.IOException;

/**
 * 登录拦截：解析 token 放进 UserContext，解析失败直接返回 401
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token == null || token.isBlank()) {
            writeUnauthorized(response, "未登录或登录已过期");
            return false;
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            UserContext.set(jwtUtil.parse(token));
            return true;
        } catch (Exception e) {
            writeUnauthorized(response, "登录状态无效，请重新登录");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }

    private void writeUnauthorized(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"msg\":\"" + msg + "\",\"data\":null}");
    }
}
