package com.ssafy.flowstudio.docs.node;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ssafy.flowstudio.api.controller.node.QuestionClassController;
import com.ssafy.flowstudio.api.controller.node.request.QuestionClassCreateRequest;
import com.ssafy.flowstudio.api.service.node.response.QuestionClassResponse;
import com.ssafy.flowstudio.api.controller.node.request.QuestionClassUpdateRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.node.QuestionClassService;
import com.ssafy.flowstudio.docs.RestDocsSupport;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.QuestionClass;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import com.ssafy.flowstudio.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QuestionClassControllerDocsTest extends RestDocsSupport {

    private final QuestionClassService questionClassService = mock(QuestionClassService.class);

    @Override
    protected Object initController() {
        return new QuestionClassController(questionClassService);
    }

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

        given(questionClassService.createQuestionClass(any()))
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
                .andDo(document("create-question-class",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Node")
                                .summary("질문 분류 (질문 클래스) 생성")
                                .requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("질문 클래스 내용")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                                .description("질문 클래스 아이디"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING)
                                                .description("질문 클래스 내용"),
                                        fieldWithPath("data.questionClassifierId").type(JsonFieldType.NUMBER)
                                                .description("질문 분류기 아이디")
                                )
                                .build())));
    }

    @DisplayName("질문 분류를 수정한다.")
    @WithMockUser
    @Test
    void updateQuestionClass() throws Exception {
        // given
        QuestionClassUpdateRequest questionClassUpdateRequest = QuestionClassUpdateRequest.builder()
                .content("question-content")
                .edgeId(1L)
                .build();

        EdgeResponse edgeResponse = EdgeResponse.builder()
                .edgeId(1L)
                .targetNodeId(2L)
                .sourceNodeId(3L)
                .build();

        QuestionClassResponse questionClassResponse = QuestionClassResponse.builder()
                .id(1L)
                .content("question-content")
                .edge(edgeResponse)
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
                .andDo(document("update-question-class",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Node")
                                .summary("질문 분류 (질문 클래스) 수정")
                                .requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("질문 클래스 내용")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                                .description("질문 클래스 아이디"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING)
                                                .description("질문 클래스 내용"),
                                        fieldWithPath("data.questionClassifierId").type(JsonFieldType.NUMBER)
                                                .description("질문 분류기 아이디")
                                )
                                .build())));
    }


    @DisplayName("질문 분류를 삭제.")
    @Test
    void deleteQuestionClass() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                delete("/api/v1/chat-flows/nodes/question-classes/{questionClassId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-question-class",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Node")
                                .summary("질문 분류 (질문 클래스) 사겢")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data").type(JsonFieldType.BOOLEAN)
                                                .description("삭제 여부")
                                )
                                .build())));
    }

}
