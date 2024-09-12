package org.example.expert.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.comment.controller.CommentAdminController;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.service.UserAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
@WebMvcTest(UserAdminController.class)
class UserAdminControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserAdminService userAdminService;

    @Test
    public void 사용자_권한_변경() throws Exception {
        //given
        long userId=1L;

        UserRoleChangeRequest userRoleChangeRequest= new UserRoleChangeRequest("USER");

        //doNothing().when(userAdminService).changeUserRole(anyLong(),userRoleChangeRequest);

        //when
        var resultActions = mockMvc.perform(patch("/admin/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsString(userRoleChangeRequest)));

        //than
        resultActions.andExpect(status().isOk());

    }

}