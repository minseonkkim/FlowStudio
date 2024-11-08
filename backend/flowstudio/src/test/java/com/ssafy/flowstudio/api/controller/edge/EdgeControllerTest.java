package com.ssafy.flowstudio.api.controller.edge;

import com.ssafy.flowstudio.api.controller.edge.request.EdgeRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.edge.request.EdgeServiceRequest;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EdgeControllerTest extends ControllerTestSupport {

    @DisplayName("간선을 만들면 응답이 반환된다.")
    @WithMockUser
    @Test
    void createEdge() throws Exception {
        EdgeRequest request = EdgeRequest.builder()
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .build();

        EdgeResponse response = EdgeResponse.builder()
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .build();

        given(edgeService.create(any(User.class), any(Long.class), any(EdgeServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/{chatFlowId}/edges", 1L)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("간선을 수정한다.")
    @WithMockUser
    @Test
    void updateEdge() throws Exception {
        EdgeRequest request = EdgeRequest.builder()
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .build();

        EdgeResponse response = EdgeResponse.builder()
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .build();

        given(edgeService.update(any(User.class), any(Long.class), any(Long.class), any(EdgeServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                put("/api/v1/chat-flows/{chatFlowId}/edges/{edgeId}", 1L, 1L)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("간선을 삭제한다.")
    @WithMockUser
    @Test
    void deleteEdge() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                delete("/api/v1/chat-flows/{chatFlowId}/edges/{edgeId}", 1L, 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

}


