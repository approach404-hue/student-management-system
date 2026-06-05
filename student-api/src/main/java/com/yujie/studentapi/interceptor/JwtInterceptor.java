package com.yujie.studentapi.interceptor;

import com.yujie.studentapi.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.trim().isEmpty()) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"请先登录\",\"data\":null}");
            return false;
        }

        if (!authorization.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"token格式错误\",\"data\":null}");
            return false;
        }

        String token = authorization.substring(7);

        try {
            Claims claims = JwtUtil.parseToken(token);

            Integer userId = claims.get("userId", Integer.class);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            request.setAttribute("role", role);

            String method = request.getMethod();

            String uri = request.getRequestURI();

            if (uri.startsWith("/users") && !"ADMIN".equals(role)) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"权限不足，只有管理员可以管理用户\",\"data\":null}");
                return false;
            }

            if (uri.startsWith("/students") && !"GET".equalsIgnoreCase(method) && !"ADMIN".equals(role)) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"权限不足，只有管理员可以操作\",\"data\":null}");
                return false;
            }

            if (!"GET".equalsIgnoreCase(method) && !"ADMIN".equals(role)) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"权限不足，只有管理员可以操作\",\"data\":null}");
                return false;
            }

            return true;
        } catch (Exception e) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"token无效或已过期\",\"data\":null}");
            return false;
        }
    }
}