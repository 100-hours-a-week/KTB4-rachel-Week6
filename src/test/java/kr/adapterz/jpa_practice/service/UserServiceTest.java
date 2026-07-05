package kr.adapterz.jpa_practice.service;

import kr.adapterz.jpa_practice.dto.user.UserRequestDto;
import kr.adapterz.jpa_practice.exception.PasswordMismatchException;
import kr.adapterz.jpa_practice.exception.DuplicateException;
import kr.adapterz.jpa_practice.repository.UserRepository;
import kr.adapterz.jpa_practice.entity.User;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("유저 service 계층 비즈니스 로직 테스트")
class UserServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode jsonScenarios;

    private UserRequestDto signupRequestDto;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService; // userService = new UserService(userRepository);


    @BeforeEach
    void setup() throws IOException {
        jsonScenarios = objectMapper.readTree(new ClassPathResource("/json/user/signup-scenarios.json").getInputStream());
    }

    private UserRequestDto getDtoByScenario(String scenarioKey) throws IOException {
        return objectMapper.treeToValue(jsonScenarios.get(scenarioKey), UserRequestDto.class);
    }


    @Test
    @DisplayName("비밀번호가 다르면 예외 발생")
    void signup_password_mismatch() throws IOException{

        //given
        // json을 dto로 읽어온다
        UserRequestDto mismatchDto = getDtoByScenario("password_mismatch");

        //when, then
        assertThrows(PasswordMismatchException.class, () -> {
            userService.createUser(mismatchDto);
        });

    }

    @Test
    @DisplayName("이메일이 중복되면 예외 발생")
    void signup_duplicate_email() throws IOException {

        // given
        UserRequestDto duplicateDto = getDtoByScenario("duplicate_email");
        User mockUser = mock(User.class);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        // when, then
        assertThrows(DuplicateException.class, () -> {
            userService.createUser(duplicateDto);
        });
    }

    @Test
    @DisplayName("닉네임이 중복되면 예외 발생")
    void signup_duplicate_nickname() throws IOException {

        // given
        UserRequestDto duplicateNicknameDto = getDtoByScenario("duplicate_nickname");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        User mockUser = mock(User.class);
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.of(mockUser));

        // when, then
        assertThrows(DuplicateException.class, () -> {
            userService.createUser(duplicateNicknameDto);
        });
    }

    @Test
    @DisplayName("회원가입 비즈니스 로직")
    void signupTest() throws IOException{

        // given
        UserRequestDto normalDto = getDtoByScenario("normal");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.empty());

        User savedUser = new User(
                normalDto.getEmail(),
                normalDto.getPassword(),
                normalDto.getNickname(),
                normalDto.getProfileImage()
        );

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        userService.createUser(normalDto);

        // then
        verify(userRepository).save(any(User.class));
    }



    @Test
    @DisplayName("로그인 로직 테스트")
    @Disabled
    void login() {



    }

    @Test
    @DisplayName("회원조회 로직 테스트")
    @Disabled
    void getUserInfo() {

    }

}