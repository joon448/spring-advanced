package org.example.expert.domain.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.common.consts.Const;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.service.ManagerService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ManagerController.class)
public class ManagerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ManagerService managerService;
    @MockBean
    JwtUtil jwtUtil;

    @Test
    void Manager_저장_성공_시_200과_응답_반환() throws Exception {
        long todoId = 10L;

        String bodyJson = """
            { "managerUserId" : 1 }
        """;

        ManagerSaveResponse resp = new ManagerSaveResponse(
                100L,
                new UserResponse(1L, "manager@test.com") // 예시
        );

        given(managerService.saveManager(any(AuthUser.class), eq(todoId), any(ManagerSaveRequest.class)))
                .willReturn(resp);

        mockMvc.perform(post("/todos/{todoId}/managers", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJson)
                        .requestAttr(Const.USERID, 1L)
                        .requestAttr(Const.EMAIL, "owner@test.com")
                        .requestAttr(Const.USERROLE, "USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.user.id").value(1L))
                .andExpect(jsonPath("$.user.email").value("manager@test.com"));
    }
}
