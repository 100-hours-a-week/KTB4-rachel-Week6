package kr.adapterz.jpa_practice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import kr.adapterz.jpa_practice.config.CorsConfig;
import kr.adapterz.jpa_practice.jwt.JwtFilter;
import kr.adapterz.jpa_practice.jwt.JwtExceptionFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtFilter jwtFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                // 1. CSRF 비활성화
                // 이 예제처럼 쿠키를 쓰지 않고, 매 요청마다 Authorization 헤더에 Bearer 토큰을 실어 보내는 구조라면
                // 브라우저가 자동으로 인증 정보를 전송하지 않기 때문에 CSRF 위험이 크게 줄어듭니다.
                // (단, 토큰을 쿠키에 저장해서 쓰는 경우에는 CSRF 보호를 유지해야 합니다.)
                .csrf(csrf -> csrf.disable())

                // 2. 폼 로그인, Basic 인증 비활성화 (우리는 토큰을 쓸 거니까)
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 3. 세션 설정: STATELESS (가장 중요!)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. 권한 설정
                // 나의 서비스에 들어오는 HTTP 요청(URL 주소)별로 어떤 권한을 가진 유저만 통과시킬지 규칙을 정하는 곳
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/signup", "/users/login", "/error").permitAll()
                        // .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // 5. 필터 추가: UsernamePasswordAuthenticationFilter 앞에 JwtFilter 배치
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtFilter.class);



        return http.build();
    }
}
