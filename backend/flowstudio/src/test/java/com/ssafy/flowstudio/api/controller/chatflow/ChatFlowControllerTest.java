package com.ssafy.flowstudio.api.controller.chatflow;

import com.ssafy.flowstudio.api.controller.chatflow.request.ChatFlowRequest;
import com.ssafy.flowstudio.api.service.chatflow.request.ChatFlowServiceRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.*;
import com.ssafy.flowstudio.api.service.node.response.AnswerResponse;
import com.ssafy.flowstudio.api.service.node.response.LlmResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.StartResponse;
import com.ssafy.flowstudio.api.service.user.response.UserResponse;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatFlowControllerTest extends ControllerTestSupport {

    @DisplayName("내 챗플로우 목록을 조회한다.")
    @WithMockUser
    @Test
    void getChatFlows() throws Exception {
        // given
        UserResponse author = UserResponse.builder()
                .id(1L)
                .username("username")
                .nickname("nickname")
                .profileImage("profileImage")
                .build();

        CategoryResponse category1 = CategoryResponse.builder()
                .categoryId(1L)
                .name("카테고리1")
                .build();

        CategoryResponse category2 = CategoryResponse.builder()
                .categoryId(2L)
                .name("카테고리2")
                .build();

        ChatFlowListResponse response = ChatFlowListResponse.builder()
                .chatFlowId(1L)
                .title("title")
                .description("description")
                .author(author)
                .thumbnail("1")
                .categories(List.of(category1, category2))
                .isPublic(false)
                .build();

        given(chatFlowService.getChatFlows(any(User.class), anyBoolean(), anyBoolean(), anyInt(), anyInt()))
                .willReturn(List.of(response));

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("챗플로우를 상세조회한다.")
    @WithMockUser
    @Test
    void getChatFlow() throws Exception {
        // given
        EdgeResponse edge1 = EdgeResponse.builder()
                .edgeId(1L)
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .build();

        EdgeResponse edge2 = EdgeResponse.builder()
                .edgeId(1L)
                .sourceNodeId(2L)
                .targetNodeId(3L)
                .build();

        NodeResponse node1 = StartResponse.builder()
                .nodeId(1L)
                .name("Start")
                .type(NodeType.START)
                .outputEdges(List.of(edge1))
                .maxLength(10)
                .build();

        NodeResponse node2 = LlmResponse.builder()
                .nodeId(2L)
                .name("LLM")
                .type(NodeType.LLM)
                .promptSystem("promptSystem")
                .promptUser("promptUser")
                .inputEdges(List.of(edge1))
                .outputEdges(List.of(edge2))
                .build();

        NodeResponse node3 = AnswerResponse.builder()
                .nodeId(3L)
                .name("Answer")
                .type(NodeType.ANSWER)
                .inputEdges(List.of(edge2))
                .outputMessage("outputMessage")
                .build();

        ChatFlowResponse response = ChatFlowResponse.builder()
                .chatFlowId(1L)
                .title("title")
                .nodes(List.of(node1, node2, node3))
                .edges(List.of(edge1, edge2))
                .publishUrl("uuid")
                .publishedAt(LocalDateTime.now())
                .build();

        given(chatFlowService.getChatFlow(any(User.class), any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/{chatFlowId}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("챗플로우를 생성한다.")
    @WithMockUser
    @Test
    void createChatFlows() throws Exception {
        // given
        ChatFlowRequest request = ChatFlowRequest.builder()
                .title("title")
                .thumbnail("thumbnail")
                .description("description")
                .categoryIds(List.of(1L, 2L))
                .build();

        // given
        EdgeResponse edge1 = EdgeResponse.builder()
                .edgeId(1L)
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .build();

        EdgeResponse edge2 = EdgeResponse.builder()
                .edgeId(1L)
                .sourceNodeId(2L)
                .targetNodeId(3L)
                .build();

        NodeResponse node1 = StartResponse.builder()
                .nodeId(1L)
                .name("Start")
                .type(NodeType.START)
                .outputEdges(List.of(edge1))
                .maxLength(10)
                .build();

        NodeResponse node2 = LlmResponse.builder()
                .nodeId(2L)
                .name("LLM")
                .type(NodeType.LLM)
                .promptSystem("promptSystem")
                .promptUser("promptUser")
                .inputEdges(List.of(edge1))
                .outputEdges(List.of(edge2))
                .build();

        NodeResponse node3 = AnswerResponse.builder()
                .nodeId(3L)
                .name("Answer")
                .type(NodeType.ANSWER)
                .inputEdges(List.of(edge2))
                .outputMessage("outputMessage")
                .build();

        ChatFlowResponse response = ChatFlowResponse.builder()
                .chatFlowId(1L)
                .title("title")
                .nodes(List.of(node1, node2, node3))
                .publishUrl("uuid")
                .publishedAt(LocalDateTime.now())
                .build();

        given(chatFlowService.createChatFlow(any(User.class), any(ChatFlowServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows")
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

    @DisplayName("챗플로우 정보를 수정한다.")
    @WithMockUser
    @Test
    void updateChatFlows() throws Exception {
        // given
        ChatFlowRequest request = ChatFlowRequest.builder()
                .title("title")
                .thumbnail("thumbnail")
                .description("description")
                .categoryIds(List.of(1L, 2L))
                .build();

        ChatFlowUpdateResponse response = ChatFlowUpdateResponse.builder()
                .chatFlowId(1L)
                .title("title")
                .description("description")
                .thumbnail("thumbnail")
                .build();

        given(chatFlowService.updateChatFlow(any(User.class), any(Long.class), any(ChatFlowServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/api/v1/chat-flows/{chatFlowId}", 1L)
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

    @DisplayName("챗플로우를 삭제한다.")
    @WithMockUser
    @Test
    void deleteChatFlows() throws Exception {
        // given

        // when
        ResultActions perform = mockMvc.perform(
                delete("/api/v1/chat-flows/{chatFlowId}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("챗플로우를 업로드한다.")
    @WithMockUser
    @Test
    void uploadChatFlows() throws Exception {
        // given
        EdgeResponse edge1 = EdgeResponse.builder()
                .edgeId(1L)
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .build();

        EdgeResponse edge2 = EdgeResponse.builder()
                .edgeId(1L)
                .sourceNodeId(2L)
                .targetNodeId(3L)
                .build();

        NodeResponse node1 = StartResponse.builder()
                .nodeId(1L)
                .name("Start")
                .type(NodeType.START)
                .outputEdges(List.of(edge1))
                .maxLength(10)
                .build();

        NodeResponse node2 = LlmResponse.builder()
                .nodeId(2L)
                .name("LLM")
                .type(NodeType.LLM)
                .promptSystem("promptSystem")
                .promptUser("promptUser")
                .inputEdges(List.of(edge1))
                .outputEdges(List.of(edge2))
                .build();

        NodeResponse node3 = AnswerResponse.builder()
                .nodeId(3L)
                .name("Answer")
                .type(NodeType.ANSWER)
                .inputEdges(List.of(edge2))
                .outputMessage("outputMessage")
                .build();

        ChatFlowResponse response = ChatFlowResponse.builder()
                .chatFlowId(1L)
                .title("title")
                .nodes(List.of(node1, node2, node3))
                .publishUrl("uuid")
                .publishedAt(LocalDateTime.now())
                .build();

        given(chatFlowService.uploadChatFlow(any(User.class), any()))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/{chatFlowId}/upload", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("챗플로우를 다운로드한다.")
    @WithMockUser
    @Test
    void downloadChatFlow() throws Exception {
        // given
        EdgeResponse edge1 = EdgeResponse.builder()
                .edgeId(1L)
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .build();

        EdgeResponse edge2 = EdgeResponse.builder()
                .edgeId(1L)
                .sourceNodeId(2L)
                .targetNodeId(3L)
                .build();

        NodeResponse node1 = StartResponse.builder()
                .nodeId(1L)
                .name("Start")
                .type(NodeType.START)
                .outputEdges(List.of(edge1))
                .maxLength(10)
                .build();

        NodeResponse node2 = LlmResponse.builder()
                .nodeId(2L)
                .name("LLM")
                .type(NodeType.LLM)
                .promptSystem("promptSystem")
                .promptUser("promptUser")
                .inputEdges(List.of(edge1))
                .outputEdges(List.of(edge2))
                .build();

        NodeResponse node3 = AnswerResponse.builder()
                .nodeId(3L)
                .name("Answer")
                .type(NodeType.ANSWER)
                .inputEdges(List.of(edge2))
                .outputMessage("outputMessage")
                .build();

        ChatFlowResponse response = ChatFlowResponse.builder()
                .chatFlowId(1L)
                .title("title")
                .nodes(List.of(node1, node2, node3))
                .publishUrl("uuid")
                .publishedAt(LocalDateTime.now())
                .build();

        given(chatFlowService.downloadChatFlow(any(User.class), any()))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/{chatFlowId}/download", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("모두의 챗플로우 목록을 조회한다.")
    @WithMockUser
    @Test
    void getEveryoneChatFlows() throws Exception {
        // given
        UserResponse author = UserResponse.builder()
                .id(1L)
                .username("username")
                .nickname("nickname")
                .profileImage("profileImage")
                .build();

        CategoryResponse category1 = CategoryResponse.builder()
                .categoryId(1L)
                .name("카테고리1")
                .build();

        CategoryResponse category2 = CategoryResponse.builder()
                .categoryId(2L)
                .name("카테고리2")
                .build();

        ChatFlowListResponse response = ChatFlowListResponse.builder()
                .chatFlowId(1L)
                .title("title")
                .description("description")
                .author(author)
                .thumbnail("1")
                .categories(List.of(category1, category2))
                .isPublic(false)
                .build();

        given(chatFlowService.getEveryoneChatFlows(0, 20))
                .willReturn(List.of(response));

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/shares")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("카테고리 목록을 조회한다.")
    @WithMockUser
    @Test
    void getCategories() throws Exception {
        // given
        CategoryResponse category1 = CategoryResponse.builder()
                .categoryId(1L)
                .name("카테고리1")
                .build();

        CategoryResponse category2 = CategoryResponse.builder()
                .categoryId(2L)
                .name("카테고리2")
                .build();

        given(chatFlowService.getCategories())
                .willReturn(List.of(category1, category2));

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/categories")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("챗플로우 실행 가능 여부를 사전 점검한다.")
    @WithMockUser
    @Test
    void precheckChatFlow() throws Exception {
        // given
        given(chatFlowService.precheck(1L))
                .willReturn(PreCheckResponse.builder().isExecutable(false).malfunctionCause("Node Number 1 resources not enough").build());

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/{chatFlowId}/precheck", 1L)
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
