package com.ssafy.flowstudio.api.controller.node;

import com.ssafy.flowstudio.api.controller.node.request.CoordinateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.AnswerUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.LlmUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.QuestionClassifierUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.RetrieverUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.StartUpdateRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.node.request.update.AnswerUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.update.LlmUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.update.QuestionClassifierUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.update.RetrieverUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.update.StartUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.response.AnswerResponse;
import com.ssafy.flowstudio.api.service.node.response.LlmResponse;
import com.ssafy.flowstudio.api.service.node.response.QuestionClassifierResponse;
import com.ssafy.flowstudio.api.service.node.response.RetrieverResponse;
import com.ssafy.flowstudio.api.service.node.response.StartResponse;
import com.ssafy.flowstudio.api.service.node.response.detail.LlmDetailResponse;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Retriever;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NodeUpdateControllerTest extends ControllerTestSupport {

    @DisplayName("시작 노드를 수정한다")
    @WithMockUser
    @Test
    void updateStart() throws Exception {
        // given
        CoordinateRequest coordinateRequest = CoordinateRequest.builder()
                .x(1)
                .y(1)
                .build();

        StartUpdateRequest request = StartUpdateRequest.builder()
                .name("updateStart")
                .coordinate(coordinateRequest)
                .maxLength(10L)
                .build();

        StartResponse response = StartResponse.builder()
                .nodeId(1L)
                .name("start")
                .type(NodeType.START)
                .coordinate(CoordinateResponse.builder()
                        .x(1)
                        .y(1)
                        .build())
                .build();

        given(nodeUpdateService.updateStart(any(User.class), any(Long.class), any(StartUpdateServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                put("/api/v1/chat-flows/nodes/{nodeId}/start", 1L)
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

    @DisplayName("질문분류기 노드를 수정한다")
    @WithMockUser
    @Test
    void updateQuestionClassifier() throws Exception {
        // given
        CoordinateRequest coordinateRequest = CoordinateRequest.builder()
                .x(1)
                .y(1)
                .build();

        QuestionClassifierUpdateRequest request = QuestionClassifierUpdateRequest.builder()
                .name("updateStart")
                .coordinate(coordinateRequest)
                .build();

        QuestionClassifierResponse response = QuestionClassifierResponse.builder()
                .nodeId(1L)
                .name("start")
                .type(NodeType.START)
                .coordinate(CoordinateResponse.builder()
                        .x(1)
                        .y(1)
                        .build())
                .build();

        given(nodeUpdateService.updateQuestionClassifier(any(User.class), any(Long.class), any(QuestionClassifierUpdateServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                put("/api/v1/chat-flows/nodes/{nodeId}/question-classifier", 1L)
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

    @DisplayName("Llm 노드를 수정한다")
    @WithMockUser
    @Test
    void updateLlm() throws Exception {
        // given
        CoordinateRequest coordinateRequest = CoordinateRequest.builder()
                .x(1)
                .y(1)
                .build();

        LlmUpdateRequest request = LlmUpdateRequest.builder()
                .name("updateStart")
                .coordinate(coordinateRequest)
                .promptSystem("promptSystem")
                .promptUser("promptUser")
                .temperature(0.5)
                .maxTokens(100)
                .context("context")
                .build();

        LlmDetailResponse response = LlmDetailResponse.builder()
                .nodeId(1L)
                .name("start")
                .type(NodeType.START)
                .promptSystem("promptSystem")
                .promptUser("promptUser")
                .temperature(0.5)
                .maxTokens(100)
                .context("context")
                .coordinate(CoordinateResponse.builder()
                        .x(1)
                        .y(1)
                        .build())
                .build();

        given(nodeUpdateService.updateLlm(any(User.class), any(Long.class), any(LlmUpdateServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                put("/api/v1/chat-flows/nodes/{nodeId}/llm", 1L)
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


    @DisplayName("Retriever 노드를 수정한다")
    @WithMockUser
    @Test
    void updateRetriever() throws Exception {
        // given
        CoordinateRequest coordinateRequest = CoordinateRequest.builder()
                .x(1)
                .y(1)
                .build();

        RetrieverUpdateRequest request = RetrieverUpdateRequest.builder()
                .name("updateStart")
                .coordinate(coordinateRequest)
                .intervalTime(10)
                .knowledgeId(1L)
                .query("query")
                .scoreThreshold(0.5f)
                .topK(10)
                .build();

        RetrieverResponse response = RetrieverResponse.builder()
                .nodeId(1L)
                .name("start")
                .type(NodeType.START)
                .coordinate(CoordinateResponse.builder()
                        .x(1)
                        .y(1)
                        .build())
                .build();

        given(nodeUpdateService.updateRetriever(any(User.class), any(Long.class), any(RetrieverUpdateServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                put("/api/v1/chat-flows/nodes/{nodeId}/retriever", 1L)
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

    @DisplayName("Answer 노드를 수정한다")
    @WithMockUser
    @Test
    void updateAnswer() throws Exception {
        // given
        CoordinateRequest coordinateRequest = CoordinateRequest.builder()
                .x(1)
                .y(1)
                .build();

        AnswerUpdateRequest request = AnswerUpdateRequest.builder()
                .name("updateStart")
                .coordinate(coordinateRequest)
                .outputMessage("outputMessage")
                .build();

        AnswerResponse response = AnswerResponse.builder()
                .nodeId(1L)
                .name("start")
                .type(NodeType.START)
                .coordinate(CoordinateResponse.builder()
                        .x(1)
                        .y(1)
                        .build())
                .build();

        given(nodeUpdateService.updateAnswer(any(User.class), any(Long.class), any(AnswerUpdateServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                put("/api/v1/chat-flows/nodes/{nodeId}/answer", 1L)
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

}
