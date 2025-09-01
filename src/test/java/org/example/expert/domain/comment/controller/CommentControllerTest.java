package org.example.expert.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.consts.Const;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    void Comment_생성_성공_시_200과_응답() throws Exception {
        long todoId = 10L;
        CommentSaveRequest req = new CommentSaveRequest("comment");

        CommentSaveResponse resp = new CommentSaveResponse(
                100L,
                "comment",
                new UserResponse(1L, "user@test.com")
        );

        given(commentService.saveComment(any(AuthUser.class), eq(todoId), any(CommentSaveRequest.class)))
                .willReturn(resp);

        mockMvc.perform(post("/todos/{todoId}/comments", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .requestAttr(Const.USERID, 1L)
                        .requestAttr(Const.EMAIL, "user@test.com")
                        .requestAttr(Const.USERROLE, "USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.contents").value("comment"))
                .andExpect(jsonPath("$.user.id").value(1L))
                .andExpect(jsonPath("$.user.email").value("user@test.com"));
    }

    @Test
    void Comment_생성_시_내용_공백이면_400() throws Exception {
        long todoId = 10L;
        CommentSaveRequest bad = new CommentSaveRequest(" ");

        mockMvc.perform(post("/todos/{todoId}/comments", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Comments_조회_성공시_200과_리스트반환() throws Exception {
        long todoId = 10L;

        List<CommentResponse> list = List.of(
                new CommentResponse(1L, "c1", new UserResponse(1L, "u1@test.com")),
                new CommentResponse(2L, "c2", new UserResponse(2L, "u2@test.com"))
        );
        given(commentService.getComments(todoId)).willReturn(list);

        mockMvc.perform(get("/todos/{todoId}/comments", todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].contents").value("c1"))
                .andExpect(jsonPath("$[0].user.id").value(1L))
                .andExpect(jsonPath("$[0].user.email").value("u1@test.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].contents").value("c2"))
                .andExpect(jsonPath("$[1].user.id").value(2L))
                .andExpect(jsonPath("$[1].user.email").value("u2@test.com"));
    }
}


