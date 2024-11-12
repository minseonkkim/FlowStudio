package com.ssafy.flowstudio.api.controller.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.flowstudio.api.controller.node.request.QuestionClassCreateRequest;
import com.ssafy.flowstudio.api.service.node.response.QuestionClassResponse;
import com.ssafy.flowstudio.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class QuestionClassControllerTest extends ControllerTestSupport {

    @DisplayName("질문 분류를 생성한다.")
    @WithMockUser
    @Test
    void createQuestionClass() throws Exception {
        // given
        QuestionClassCreateRequest questionClassCreateRequest = QuestionClassCreateRequest.builder()
                .content("question-content")
                .build();

        QuestionClassResponse questionClassResponse = QuestionClassResponse.builder()
                .id(1L)
                .content("question-content")
                .edge(null)
                .questionClassifierId(1L)
                .build();

        given(questionClassService.createQuestionClass(any(), any()))
                .willReturn(questionClassResponse);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/nodes/{nodeId}/question-classes", 1)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(questionClassCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.content").value("question-content"))
                .andExpect(jsonPath("$.data.questionClassifierId").value(1L));
    }

    @DisplayName("질문 분류를 수정한다.")
    @WithMockUser
    @Test
    void updateQuestionClass() throws Exception {
        // given
        QuestionClassCreateRequest questionClassUpdateRequest = QuestionClassCreateRequest.builder()
                .content("question-content")
                .build();

        QuestionClassResponse questionClassResponse = QuestionClassResponse.builder()
                .id(1L)
                .content("question-content")
                .edge(null)
                .questionClassifierId(1L)
                .build();

        given(questionClassService.updateQuestionClass(any(), any()))
                .willReturn(questionClassResponse);

        // when
        ResultActions perform = mockMvc.perform(
                put("/api/v1/chat-flows/nodes/question-classes/{questionClassId}", 1)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(questionClassUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.content").value("question-content"))
                .andExpect(jsonPath("$.data.questionClassifierId").value(1L));
    }
}