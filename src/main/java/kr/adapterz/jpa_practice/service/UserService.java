package kr.adapterz.jpa_practice.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import kr.adapterz.jpa_practice.jwt.JwtTokenProvider;
import kr.adapterz.jpa_practice.dto.auth.TokenResponseDto;
import kr.adapterz.jpa_practice.dto.user.*;
import kr.adapterz.jpa_practice.entity.User;
import kr.adapterz.jpa_practice.exception.DuplicateException;
import kr.adapterz.jpa_practice.exception.NotFoundException;
import kr.adapterz.jpa_practice.exception.PasswordMismatchException;
import kr.adapterz.jpa_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;


@Service
@Validated
@RequiredArgsConstructor // 생성자 자동 생성
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider; // 여기 접근제어자 뭐야


    @Transactional
    public UserResponseDto createUser(UserRequestDto request){

        // 비밀번호 확인 로직
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new PasswordMismatchException("PASSWORD_MISMATCH");
        }

        // 이메일 중복 검사
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateException("EMAIL_ALREADY_EXISTS");
        }

        // 닉네임 중복 검사
        if(userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new DuplicateException("NickName_ALREADY_EXISTS");
        }


        User user = new User(
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                request.getProfileImage()
        );

        User savedUser = userRepository.save(user); // userResponsitory가 인터페이스, User타입으로 구현체
        return new UserResponseDto(savedUser);
    }


    public UserAllResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));
        return new UserAllResponseDto(user);
    }


    @Transactional
    public UserUpdateResponseDto updateUserInfo(
            @Positive Long userId, // @Positive는 userId가 0보다 큰 값인지를 검증
            @Valid UserUpdateRequestDto request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        // 중복 이메일 방지
        userRepository.findByNickname(request.getNickname())
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
                });

        user.changeProfileImage(request.getProfileImage());
        user.changeNickname(request.getNickname());

        //TODO: nickname은 반정규화. 동기화 시켜야줘야 한다.
        // user 엔티티에서는 nickname이 변경됨. 근데 연관관계인 Post, Comment에는 복사해서 사용하였다. 이때 동기화 로직이 필요하다.

        return new UserUpdateResponseDto(user);
    }


    @Transactional
    public UserResponseDto updatePassword(
            @Positive Long userId,
            @Valid PasswordUpdateRequestDto request
            ) {

        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new PasswordMismatchException("PASSWORD_MISMATCH");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));
        user.changePassword(request.getPassword());

        return new UserResponseDto(user);
    }


    @Transactional
    public UserResponseDto deleteUser(Long userId) {
        // 수정사항: 삭제하기 전에 먼저 user 조회 해야 한다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        user.delete();

        return new UserResponseDto(user);
    }


    public TokenResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new PasswordMismatchException("PASSWORD_MISMATCH");
        }

        // 인증 성공 후 시큐리티 인증 정보 생성
        org.springframework.security.core.Authentication authentication =
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        user.getEmail(), // UsernamePasswordAuthenticationToken이 뭐길래 이메일을 보내? 아님 가져오는건가
                        null, // 원래는 무슨자리인데 왜 null을 보내
                        org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_USER")
                );

        // 토큰 발행
        String accessToken = jwtTokenProvider.createToken(user.getEmail());

        return TokenResponseDto.builder() // 그럼 로그인할때마다 새로운 객체로 안만들어지는거야..?
                    .grantType("Bearer")
                    .accessToken(accessToken)
                    .refreshToken("추후 구현 예정")
                    .userInfo(new UserResponseDto(user)) // 유저 객체 쏙 대입
                    .build();
        }

}
