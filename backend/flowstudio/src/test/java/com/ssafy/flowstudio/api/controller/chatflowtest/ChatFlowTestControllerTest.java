package com.ssafy.flowstudio.api.controller.chatflowtest;

import com.ssafy.flowstudio.api.controller.chatflowtest.request.ChatFlowTestRequest;
import com.ssafy.flowstudio.api.service.chat.response.ChatDetailResponse;
import com.ssafy.flowstudio.api.service.chat.response.ChatListResponse;
import com.ssafy.flowstudio.api.service.chat.response.ChatSimpleResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatFlowTestControllerTest extends ControllerTestSupport {
//
//    @DisplayName("챗플로우 테스트 목록 조회")
//    @WithMockUser
//    @Test
//    void getChats() throws Exception {
//        // given
//
//        // when
//        ResultActions perform = mockMvc.perform(
//                get("/api/v1/chat-flows/{chatFlowId}/tests", 1L)
//                        .contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        perform.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("200"))
//                .andExpect(jsonPath("$.status").value("OK"))
//                .andExpect(jsonPath("$.message").value("OK"))
//                .andExpect(jsonPath("$.data").exists());
//    }
//
//
//    @DisplayName("챗플로우 테스트 상세 조회")
//    @WithMockUser
//    @Test
//    void getChat() throws Exception {
//        // given
//
//        // when
//        ResultActions perform = mockMvc.perform(
//                get("/api/v1/chat-flows/{chatFlowId}/tests/{chatFlowTestId}", 1L, 1L)
//                        .contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        perform.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("200"))
//                .andExpect(jsonPath("$.status").value("OK"))
//                .andExpect(jsonPath("$.message").value("OK"))
//                .andExpect(jsonPath("$.data").exists());
//    }

    @DisplayName("챗플로우 테스트 생성")
    @WithMockUser
    @Test
    void createChat() throws Exception {
        // given
        ChatFlowTestRequest request1 = ChatFlowTestRequest.builder()
                .groundTruth("groundTruth")
                .testQuestion("testQuestion")
                .build();

        ChatFlowTestRequest request2 = ChatFlowTestRequest.builder()
                .groundTruth("groundTruth")
                .testQuestion("testQuestion")
                .build();

        given(chatFlowTestService.createChatFlowTest(any(User.class), any(Long.class), any()))
                .willReturn(List.of(1L, 2L));

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/{chatFlowId}/tests", 1L)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(List.of(request1, request2)))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

}
