package org.example.expert.domain.user.service;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAdminService userAdminService;

    @Test
    public void 사용자_권한변경_정상동작(){
        //given
        long userId=1L;
        UserRoleChangeRequest userRoleChangeRequest=new UserRoleChangeRequest("ADMIN");

        User user = new User("email", "pwd", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        //when
        userAdminService.changeUserRole(userId,userRoleChangeRequest);

        //than
        Assertions.assertEquals(UserRole.ADMIN,user.getUserRole());
    }

    @Test
    public void 사용자_권한변경_Entity_없음(){
        //given
        long userId=1L;
        UserRoleChangeRequest userRoleChangeRequest=new UserRoleChangeRequest("ADMIN");

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        InvalidRequestException exception=assertThrows(InvalidRequestException.class,
                ()-> userAdminService.changeUserRole(userId,userRoleChangeRequest));

        //than
        Assertions.assertEquals("User not found",exception.getMessage());
        verify(userRepository,times(1)).findById(userId);

    }
}