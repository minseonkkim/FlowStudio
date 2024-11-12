package com.ssafy.flowstudio.api.controller.chat;

import com.ssafy.flowstudio.api.service.chat.response.ChatListResponse;
import com.ssafy.flowstudio.api.service.chat.response.ChatSimpleResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.CategoryResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowListResponse;
import com.ssafy.flowstudio.api.service.user.response.UserResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatControllerTest extends ControllerTestSupport {


    @DisplayName("채팅 목록 조회")
    @WithMockUser
    @Test
    void getChatFlows() throws Exception {
        // given
        ChatSimpleResponse chat1 = ChatSimpleResponse.builder()
                .id(1L)
                .title("title1")
                .build();

        ChatSimpleResponse chat2 = ChatSimpleResponse.builder()
                .id(2L)
                .title("title2")
                .build();

        ChatListResponse response = ChatListResponse.builder()
                .id(1L)
                .title("title")
                .thumbnail("1")
                .chats(List.of(chat1, chat2))
                .build();

        given(chatService.getChats(any(User.class), any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/{chatFlowId}/chats", 1L)
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