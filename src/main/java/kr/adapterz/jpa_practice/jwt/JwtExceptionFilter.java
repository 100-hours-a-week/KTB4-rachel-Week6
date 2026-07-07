package kr.adapterz.jpa_practice.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal (
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {

            setErrorResponse(response, "JWT_EXPIRED", "토큰이 만료되었습니다.");

        } catch (MalformedJwtException e) {

            setErrorResponse(response, "JWT_INVALID", "잘못된 JWT입니다.");

        } catch (SecurityException e) {

            setErrorResponse(response, "JWT_SIGNATURE_INVALID", "JWT 서명이 올바르지 않습니다.");

        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            String code,
            String message
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> body = new HashMap<>();
        body.put("code", code);
        body.put("message", message);

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
