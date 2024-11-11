package com.ssafy.flowstudio.docs.edge;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ssafy.flowstudio.api.controller.edge.EdgeController;
import com.ssafy.flowstudio.api.controller.edge.request.EdgeRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.edge.EdgeService;
import com.ssafy.flowstudio.api.service.edge.request.EdgeServiceRequest;
import com.ssafy.flowstudio.docs.RestDocsSupport;
import com.ssafy.flowstudio.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EdgeControllerDocsTest extends RestDocsSupport {

    private final EdgeService edgeService = mock(EdgeService.class);

    @Override
    protected Object initController() {
        return new EdgeController(edgeService);
    }

    @DisplayName("간선을 생성한다.")
    @Test
    void createEdge() throws Exception {
        // given
        EdgeRequest request = EdgeRequest.builder()
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .sourceConditionId(1L)
                .build();

        EdgeResponse response = EdgeResponse.builder()
                .edgeId(1L)
                .sourceNodeId(2L)
                .targetNodeId(1L)
                .sourceConditionId(1L)
                .build();

        given(edgeService.create(any(User.class), any(Long.class), any(EdgeServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/{chatFlowId}/edges", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("create-edge",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Edge")
                                .summary("간선 생성")
                                .requestFields(
                                        fieldWithPath("sourceNodeId").type(JsonFieldType.NUMBER)
                                                .description("시작 노드 아이디"),
                                        fieldWithPath("targetNodeId").type(JsonFieldType.NUMBER)
                                                .description("도착 노드 아이디"),
                                        fieldWithPath("sourceConditionId").type(JsonFieldType.NUMBER)
                                                .description("시작 조건 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.edgeId").type(JsonFieldType.NUMBER)
                                                .description("간선 아이디"),
                                        fieldWithPath("data.sourceNodeId").type(JsonFieldType.NUMBER)
                                                .description("시작 노드 아이디"),
                                        fieldWithPath("data.targetNodeId").type(JsonFieldType.NUMBER)
                                                .description("도착 노드 아이디"),
                                        fieldWithPath("data.sourceConditionId").type(JsonFieldType.NUMBER)
                                                .description("시작 조건 아이디")
                                )
                                .build())));
    }

    @DisplayName("간선을 수정한다.")
    @Test
    void updateEdge() throws Exception {
        // given
        EdgeRequest request = EdgeRequest.builder()
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .sourceConditionId(1L)
                .build();

        EdgeResponse response = EdgeResponse.builder()
                .edgeId(1L)
                .sourceNodeId(2L)
                .targetNodeId(1L)
                .sourceConditionId(1L)
                .build();

        given(edgeService.update(any(User.class), any(Long.class), any(Long.class), any(EdgeServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                put("/api/v1/chat-flows/{chatFlowId}/edges/{edgeId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-edge",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Edge")
                                .summary("간선 수정")
                                .requestFields(
                                        fieldWithPath("sourceNodeId").type(JsonFieldType.NUMBER)
                                                .description("시작 노드 아이디"),
                                        fieldWithPath("targetNodeId").type(JsonFieldType.NUMBER)
                                                .description("도착 노드 아이디"),
                                        fieldWithPath("sourceConditionId").type(JsonFieldType.NUMBER)
                                                .description("시작 조건 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.edgeId").type(JsonFieldType.NUMBER)
                                                .description("간선 아이디"),
                                        fieldWithPath("data.sourceNodeId").type(JsonFieldType.NUMBER)
                                                .description("시작 노드 아이디"),
                                        fieldWithPath("data.targetNodeId").type(JsonFieldType.NUMBER)
                                                .description("도착 노드 아이디"),
                                        fieldWithPath("data.sourceConditionId").type(JsonFieldType.NUMBER)
                                                .description("시작 조건 아이디")
                                )
                                .build())));
    }

    @DisplayName("간선을 삭제한다.")
    @Test
    void deleteEdge() throws Exception {
        // given
        EdgeRequest request = EdgeRequest.builder()
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .sourceConditionId(1L)
                .build();

        // when
        ResultActions perform = mockMvc.perform(
                delete("/api/v1/chat-flows/{chatFlowId}/edges/{edgeId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-edge",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Edge")
                                .summary("간선 삭제")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data").type(JsonFieldType.NULL)
                                                .description("데이터")
                                )
                                .build())));
    }
}
