package kr.adapterz.jpa_practice.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kr.adadpterz.springboot_project.dto.user.*;
import kr.adadpterz.springboot_project.entity.User;
import kr.adadpterz.springboot_project.exception.DuplicateException;
import kr.adadpterz.springboot_project.exception.NotFoundException;
import kr.adadpterz.springboot_project.exception.PasswordMismatchException;
import kr.adadpterz.springboot_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Service
@Validated
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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


    public UserUpdateResponseDto updateUserInfo(
            @Positive Long userId, // @Positive는 userId가 0보다 큰 값인지를 검증
            @Valid UserUpdateRequestDto request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));


        user.changeProfileImage(request.getProfileImage());
        user.changeNickname(request.getNickname());

        return new UserUpdateResponseDto(user);
    }

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




    public UserResponseDto deleteUser(Long userId) {
        User user = userRepository.deleteUser(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

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
