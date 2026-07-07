package kr.adapterz.jpa_practice.controller;

import jakarta.validation.Valid;
import kr.adapterz.jpa_practice.dto.user.*;
import kr.adapterz.jpa_practice.dto.auth.TokenResponseDto;
import kr.adapterz.jpa_practice.response.ApiResponse;
import kr.adapterz.jpa_practice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// @CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser (
            @Valid @RequestBody UserRequestDto request
    ) {
        UserResponseDto result = userService.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/users/signup" + result.getUserId())
                .body(ApiResponse.of("USER_CREATED",result, null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDto>> login (
            @Valid @RequestBody LoginRequestDto request
    ) {
        TokenResponseDto tokenResult = userService.login(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Authorization", tokenResult.getGrantType() + " " + tokenResult.getAccessToken())
                .body(ApiResponse.of("LOGIN_SUCCESS", tokenResult.getUserInfo(), null));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserUpdateResponseDto>> updateNicknameProfileImg(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequestDto request
    ) {
        UserUpdateResponseDto result = userService.updateUserInfo(userId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("NICKNAME_IMAGE_UPDATED", result,null));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<ApiResponse<UserResponseDto>> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody PasswordUpdateRequestDto request
    ) {
        UserResponseDto result = userService.updatePassword(userId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("NICKNAME_UPDATED", result));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserAllResponseDto>> getUser(
            @PathVariable Long userId
    ) {
        UserAllResponseDto result = userService.getUser(userId);
        return ResponseEntity.ok(
                ApiResponse.of("USER_RETRIEVED", result)
        );
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> deleteUser(
            @PathVariable Long userId
    ) {
        UserResponseDto result = userService.deleteUser(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("USER_DELETED", result));
    }
}
