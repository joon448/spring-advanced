package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.errorcode.UserErrorCode;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void signup_성공_시_Bearer_토큰_반환() {
        // given
        SignupRequest request = new SignupRequest("a@test.com", "rawPW", "USER");

        given(userRepository.existsByEmail("a@test.com")).willReturn(false);
        given(passwordEncoder.encode("rawPW")).willReturn("ENCODED_PW");

        User user = new User("a@test.com", "ENCODED_PW", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.save(any(User.class))).willReturn(user);
        given(jwtUtil.createToken(1L, "a@test.com", UserRole.USER)).willReturn("Bearer TOKEN");

        // when & then
        SignupResponse response = authService.signup(request);

        assertNotNull(response);
        assertEquals("Bearer TOKEN", response.getBearerToken());

        verify(userRepository).existsByEmail("a@test.com");
    }

    @Test
    void signup_시_이메일_중복_예외_처리() {
        // given
        SignupRequest request = new SignupRequest("dup@test.com", "PW", "USER");
        given(userRepository.existsByEmail("dup@test.com")).willReturn(true);

        // when
        InvalidRequestException ex = assertThrows(
                InvalidRequestException.class,
                () -> authService.signup(request)
        );

        // then
        assertEquals(UserErrorCode.DUPLICATED_EMAIL, ex.getErrorCode());
        verify(userRepository).existsByEmail("dup@test.com");
    }

    @Test
    void signin_성공_시_Bearer_토큰_반환() {
        // given
        SigninRequest request = new SigninRequest("a@test.com", "rawPW");
        User user = new User("a@test.com", "ENCODED_PW", UserRole.ADMIN);
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findByEmail("a@test.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("rawPW", "ENCODED_PW")).willReturn(true);
        given(jwtUtil.createToken(1L, "a@test.com", UserRole.ADMIN)).willReturn("Bearer Token");

        // when
        SigninResponse response = authService.signin(request);

        // then
        assertNotNull(response);
        assertEquals("Bearer Token", response.getBearerToken());
    }

    @Test
    void signin_시_미등록_이메일_예외_처리() {
        // given
        SigninRequest request = new SigninRequest("nouser@test.com", "rawPW");
        given(userRepository.findByEmail("nouser@test.com")).willReturn(Optional.empty());

        // when
        InvalidRequestException ex = assertThrows(
                InvalidRequestException.class,
                () -> authService.signin(request)
        );

        // then
        assertEquals(UserErrorCode.USER_NOT_REGISTERED, ex.getErrorCode());
    }

    @Test
    void signin_시_비밀번호_불일치_예외_처리() {
        // given
        SigninRequest request = new SigninRequest("user@test.com", "wrongPW");
        User user = new User("user@test.com", "ENCODED_PW", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findByEmail("user@test.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrongPW", "ENCODED_PW")).willReturn(false);

        // when
        AuthException ex = assertThrows(
                AuthException.class,
                () -> authService.signin(request)
        );

        // then
        assertEquals(UserErrorCode.WRONG_PASSWORD, ex.getErrorCode());
    }
}
