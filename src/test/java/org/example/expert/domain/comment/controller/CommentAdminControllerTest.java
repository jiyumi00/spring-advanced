package org.example.expert.domain.comment.controller;

import org.assertj.core.api.Assertions;
import org.example.expert.aop.Aspect;
import org.example.expert.domain.comment.service.CommentAdminService;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.sql.Ref;

import static org.awaitility.Awaitility.given;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
@WebMvcTest(CommentAdminController.class)
class CommentAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentAdminService commentAdminService;

    @Test
    public void 어드민_댓글_삭제(CapturedOutput output) throws Exception{
        //given
        long commentId=1L;
        long userId=2L;

        doNothing().when(commentAdminService).deleteComment(commentId);

        //when
        ResultActions resultActions=mockMvc.perform(delete("/admin/comments/{commentId}",commentId)
                 .requestAttr("userId",userId));

        resultActions.andExpect(status().isOk());

        //then
        Mockito.verify(commentAdminService,Mockito.times(1)).deleteComment(commentId);
    }

}