package org.example.expert.domain.user.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.errorcode.UserErrorCode;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void user_조회_시_user가_없다면_InvalidRequestException_에러를_던진다() {
        // given
        long userId = 1L;
        given(userRepository.findByIdOrElseThrow(userId))
                .willThrow(new InvalidRequestException(UserErrorCode.USER_NOT_FOUND));

        // when & then
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> userService.getUser(userId));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void user_조회_시_user가_있다면_해당_user를_반환한다() {
        // given
        long userId = 1L;
        User user = new User("a@a.com", "password", UserRole.USER);
        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);

        // when & then
        UserResponse foundUser = userService.getUser(userId);

        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    public void 비밀번호_변경_성공(){
        // given
        long userId = 1L;
        User user = new User("a@a.com", "HASH_OLD", UserRole.USER);
        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);

        // 새 비번이 기존과 다름
        given(passwordEncoder.matches("new123!!", "HASH_OLD")).willReturn(false);
        // 기존 비번 일치
        given(passwordEncoder.matches("old123!!", "HASH_OLD")).willReturn(true);
        // 인코딩 결과
        given(passwordEncoder.encode("new123!!")).willReturn("HASH_NEW");

        // when & then
        userService.changePassword(userId, new UserChangePasswordRequest("old123!!", "new123!!"));

        assertEquals("HASH_NEW", user.getPassword());
        verify(passwordEncoder).matches("new123!!", "HASH_OLD");
        verify(passwordEncoder).matches("old123!!", "HASH_OLD");
        verify(passwordEncoder).encode("new123!!");
    }

    @Test
    void 비밀번호_변경_시_새_비밀번호가_기존과_같으면_예외_발생() {
        // given
        long userId = 1L;
        User user = new User("a@a.com", "HASH_OLD", UserRole.USER);
        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);

        given(passwordEncoder.matches("samePW", "HASH_OLD")).willReturn(true);

        // when
        InvalidRequestException ex = assertThrows(
                InvalidRequestException.class,
                () -> userService.changePassword(userId, new UserChangePasswordRequest("HASH_OLD", "samePW"))
        );

        // then
        assertEquals(UserErrorCode.PASSWORD_SAME_AS_OLD, ex.getErrorCode());

        verify(passwordEncoder).matches("samePW", "HASH_OLD");
    }

    @Test
    void 비밀번호_변경_시_기존_비밀번호_불일치면_예외() {
        // given
        long userId = 1L;
        User user = new User("a@a.com", "HASH_OLD", UserRole.USER);
        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);

        given(passwordEncoder.matches("newPW", "HASH_OLD")).willReturn(false);
        given(passwordEncoder.matches("wrongPW", "HASH_OLD")).willReturn(false);

        // when & then
        InvalidRequestException ex = assertThrows(
                InvalidRequestException.class,
                () -> userService.changePassword(userId, new UserChangePasswordRequest("wrongPW", "newPW"))
        );

        assertEquals(UserErrorCode.WRONG_PASSWORD, ex.getErrorCode());
    }
}
