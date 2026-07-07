package kr.adapterz.jpa_practice.service;

import kr.adapterz.jpa_practice.entity.User;
import kr.adapterz.jpa_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("이메일로 사용자를 찾을 수 없습니다: " + username));

        // Spring Security의 UserDetails 객체로 변환
        return org.springframework.security.core.userdetails.User
                .builder() // 심볼 'builder'을(를) 해결할 수 없습니다
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();


    }
}
