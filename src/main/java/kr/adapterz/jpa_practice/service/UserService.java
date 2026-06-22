package kr.adapterz.jpa_practice.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;


    @Transactional
    public UserResponseDto createUser(UserRequestDto request){

        // 비밀번호 확인 로직
        if (!request.getPassword().equals(request.getPasswordCheck())) { // TODO: 비밀번호 확인하는 메서드 만들어야 하나? - 굳이.. 회원가입할때, 변경할때만 if문 이용하므로. 그치만 따로 뺀다면 어떻게 빼야하지?
            throw new PasswordMismatchException("PASSWORD_MISMATCH");
        }

        // 이메일 중복 검사
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {// 이미 있는 이메일이라면
            throw new DuplicateException("EMAIL_ALREADY_EXISTS");
        }

        // 닉네임 중복 검사
        if(userRepository.findByNickname(request.getNickname()).isPresent()) {// 이미 있는 이메일이라면
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


    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));
        return new UserResponseDto(user);
    }


    @Transactional
    public UserUpdateResponseDto updateUserInfo(
            @Positive Long userId, // @Positive는 userId가 0보다 큰 값인지를 검증
            @Valid UserUpdateRequestDto request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));


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


    public UserResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new PasswordMismatchException("PASSWORD_MISMATCH");
        }
        return new UserResponseDto(user);
    }

}
