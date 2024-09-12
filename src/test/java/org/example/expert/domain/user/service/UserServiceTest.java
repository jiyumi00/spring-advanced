package org.example.expert.domain.user.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Nested
    class getUserTest{
        @Test
        public void 유저_조회_정상동작(){
            //given
            long userId=1L;
            User user = new User("email", "pwd", UserRole.USER);
            ReflectionTestUtils.setField(user, "id", userId);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            //when
            UserResponse userResponse=userService.getUser(userId);

            //than
            Assertions.assertNotNull(userResponse);
            Assertions.assertEquals("email",userResponse.getEmail());
        }

        @Test
        public void 유저_조회_entity_없음(){
            //given
            long userId=1L;
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            //when
            InvalidRequestException exception=assertThrows(InvalidRequestException.class,()-> userService.getUser(userId));

            //than
            assertEquals("User not found",exception.getMessage());

        }
    }

    @Nested
    class changePasswordTest{
        @Test
        public void 비밀번호_변경_정상동작(){
            //given
            long userId=1L;
            UserChangePasswordRequest userChangePasswordRequest=new UserChangePasswordRequest("abcd","1234");

            User user = new User("email", "pwd", UserRole.USER);
            ReflectionTestUtils.setField(user, "id", userId);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));


            //when
            userService.changePassword(userId,userChangePasswordRequest);

            //than

        }
    }
}