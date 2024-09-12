package org.example.expert.domain.user.service;

import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

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
        UserRoleChangeRequest userRoleChangeRequest=new UserRoleChangeRequest("USER");

        User user=new User();

        //given(userRepository.findById(anyLong())).willReturn();
        //when

        userAdminService.changeUserRole(userId,userRoleChangeRequest);
        //than
    }
}