package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private TodoService todoService;

    @org.junit.jupiter.api.Nested
    class GetTodoTest{
        @Test
        public void 일정_단건_조회_정상동작() {
            //given
            long todoId = 1L;
            long userId = 2L;

            User user = new User("email", "pwd", UserRole.USER);
            ReflectionTestUtils.setField(user, "id", userId);

            Todo todo = new Todo("오늘 기분", "너무 행복해", "맑음", user);
            ReflectionTestUtils.setField(todo, "id", todoId);

            given(todoRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(todo));
            //when

            TodoResponse todoResponse = todoService.getTodo(todoId);

            //then

            Assertions.assertNotNull(todoResponse);
            Assertions.assertEquals(1, todoResponse.getId());

        }

        @Test
        public void 일정_단건_조회_entity_없음() {
            //given
            long todoId = 1L;

            given(todoRepository.findByIdWithUser(anyLong())).willReturn(Optional.empty());

            //when
            InvalidRequestException exception = Assertions.assertThrows(InvalidRequestException.class, () -> todoService.getTodo(todoId));

            //than
            Assertions.assertEquals("Todo not found", exception.getMessage());
        }
    }

}