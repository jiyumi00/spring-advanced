package org.example.expert.domain.user.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Nested
    class getUserTest {
        private User user;
        private long userId;

        @BeforeEach
        public void setUser() {
            userId = 1L;
            user = new User("email", "pwd", UserRole.USER);
            ReflectionTestUtils.setField(user, "id", userId);
        }

        @Test
        public void 유저_조회_정상동작() {
            //given
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            //when
            UserResponse userResponse = userService.getUser(userId);

            //than
            Assertions.assertNotNull(userResponse);
            Assertions.assertEquals("email", userResponse.getEmail());
        }

        @Test
        public void 유저_조회_entity_없음() {
            //given
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            //when
            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> userService.getUser(userId));

            //than
            assertEquals("User not found", exception.getMessage());

        }
    }

    @Nested
    class changePasswordTest {
        private long userId;
        private String oldPassword;
        private String newPassword;
        private UserChangePasswordRequest userChangePasswordRequest;
        private User user;

        @BeforeEach
        public void setNewPassword(){
            userId = 1L;
            oldPassword="Abc1234!";
            newPassword="Bcd1234*";
            userChangePasswordRequest = new UserChangePasswordRequest(oldPassword, newPassword);
            user = mock(User.class);
            ReflectionTestUtils.setField(user, "id", userId);
        }
        @Test
        public void 비밀번호_변경_정상동작() {
            //given
            //userRepository
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            //passWordEncoder
            given(passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())).willReturn(false);
            given(passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())).willReturn(true);
            given(passwordEncoder.encode(userChangePasswordRequest.getNewPassword())).willReturn("newEncodedPassword");

            //when
            userService.changePassword(userId, userChangePasswordRequest);

            //that
            verify(userRepository,times(1)).findById(userId);
            verify(passwordEncoder,times(1)).matches(userChangePasswordRequest.getNewPassword(),user.getPassword());
            verify(passwordEncoder,times(1)).matches(userChangePasswordRequest.getOldPassword(),user.getPassword());
            verify(passwordEncoder,times(1)).encode(userChangePasswordRequest.getNewPassword());

            verify(user,times(1)).changePassword("newEncodedPassword");
        }

        @Test
        public void 비밀번호_변경_실패_새밀번호_기존과_동일(){
            //given
            //userRepository
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            //when - 새비밀번호 기존과 동일
            given(passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())).willReturn(true);

            //than
            InvalidRequestException invalidRequestException=assertThrows(InvalidRequestException.class,()->userService.changePassword(userId,userChangePasswordRequest));
            assertEquals("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.",invalidRequestException.getMessage());
        }
    }
}