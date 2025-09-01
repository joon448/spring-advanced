package org.example.expert.domain.user.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.errorcode.UserErrorCode;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserAdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserAdminService userAdminService;

    @Test
    public void userrole_변경_성공() {
        // given
        long userId = 1L;
        User user = new User("a@a.com", "rawPW", UserRole.USER);
        given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);

        UserRoleChangeRequest request = new UserRoleChangeRequest("ADMIN");

        // when & then
        assertDoesNotThrow(() -> userAdminService.changeUserRole(userId, request));
        assertEquals(UserRole.ADMIN, user.getUserRole());
    }

    @Test
    public void userrole_변경_시_user가_없다면_예외_발생() {
        // given
        long userId = 1L;
        User user = new User("a@a.com", "rawPW", UserRole.USER);
        given(userRepository.findByIdOrElseThrow(userId))
                .willThrow(new InvalidRequestException(UserErrorCode.USER_NOT_FOUND));

        UserRoleChangeRequest request = new UserRoleChangeRequest("ADMIN");

        // when & then
        InvalidRequestException ex = assertThrows(
                InvalidRequestException.class,
                () -> userAdminService.changeUserRole(userId, request)
        );
        assertEquals(UserErrorCode.USER_NOT_FOUND, ex.getErrorCode());
    }
}
