package com.ssafy.flowstudio.docs.node;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ssafy.flowstudio.api.controller.node.NodeController;
import com.ssafy.flowstudio.api.controller.node.request.CoordinateRequest;
import com.ssafy.flowstudio.api.controller.node.request.NodeCreateRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.node.NodeService;
import com.ssafy.flowstudio.api.service.node.request.NodeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.node.response.NodeCreateResponse;
import com.ssafy.flowstudio.api.service.node.response.SimpleNodeResponse;
import com.ssafy.flowstudio.api.service.node.response.detail.AnswerDetailResponse;
import com.ssafy.flowstudio.api.service.node.response.detail.StartDetailResponse;
import com.ssafy.flowstudio.docs.RestDocsSupport;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Start;
import com.ssafy.flowstudio.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NodeControllerDocsTest extends RestDocsSupport {

    private final NodeService nodeService = mock(NodeService.class);

    @Override
    protected Object initController() {
        return new NodeController(nodeService);
    }

    @DisplayName("노드를 생성한다.")
    @Test
    void createNode() throws Exception {
        // given
        NodeCreateRequest request = NodeCreateRequest.builder()
                .chatFlowId(1L)
                .coordinate(CoordinateRequest.builder()
                        .x(1)
                        .y(1)
                        .build())
                .type(NodeType.START)
                .build();

        AnswerDetailResponse answerNodeDetailResponse = AnswerDetailResponse.builder()
                .nodeId(1L)
                .name("Answer")
                .type(NodeType.ANSWER)
                .outputMessage("AI 답변 템플릿")
                .precedingNodes(new ArrayList<>())
                .coordinate(CoordinateResponse.from(Coordinate.builder().x(1.0F).y(1.0F).build()))
                .build();

        given(nodeService.createNode(any(User.class), any(NodeCreateServiceRequest.class)))
                .willReturn(answerNodeDetailResponse);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/nodes")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("create-node",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Node")
                                .summary("노드 생성")
                                .requestFields(
                                        fieldWithPath("chatFlowId").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 아이디"),
                                        fieldWithPath("coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("x좌표"),
                                        fieldWithPath("coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("y좌표"),
                                        fieldWithPath("type").type(JsonFieldType.STRING)
                                                .description("노드 타입")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.nodeId").type(JsonFieldType.NUMBER)
                                                .description("노드 아이디"),
                                        fieldWithPath("data.type").type(JsonFieldType.STRING)
                                                .description("노드 타입"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                                                .description("노드 이름"),
                                        fieldWithPath("data.coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("노드 X좌표"),
                                        fieldWithPath("data.coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("노드 Y좌표"),
                                        fieldWithPath("data.inputEdges").type(JsonFieldType.NULL)
                                                .description("진입 간선들의 리스트"),
                                        fieldWithPath("data.outputEdges").type(JsonFieldType.NULL)
                                                .description("진출 간선들의 리스트"),
                                        fieldWithPath("data.precedingNodes[]").type(JsonFieldType.ARRAY)
                                                .description("선행 노드"),
                                        fieldWithPath("data.outputMessage").type(JsonFieldType.STRING)
                                                .description("Answer 노드의 내용")
                                )
                                .build())));
    }

    @DisplayName("노드를 삭제한다.")
    @Test
    void deleteNode() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                delete("/api/v1/chat-flows/nodes/{nodeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-node",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Node")
                                .summary("노드 삭제")
                                .pathParameters(
                                        parameterWithName("nodeId").description("노드 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data").type(JsonFieldType.NULL)
                                                .description("data")
                                )
                                .build())));
    }

    @DisplayName("노드 아이디로 노드의 상세 정보를 조회한다.")
    @Test
    void getNode() throws Exception {
        AnswerDetailResponse answerNodeDetailResponse = AnswerDetailResponse.builder()
                .nodeId(1L)
                .name("Answer")
                .type(NodeType.ANSWER)
                .outputMessage("AI 답변 템플릿")
                .inputEdges(List.of(
                        EdgeResponse.builder().edgeId(1L).sourceNodeId(2L).targetNodeId(1L).build()
                ))
                .precedingNodes(List.of(
                        SimpleNodeResponse.builder().nodeId(2L).name("선행 노드").type(NodeType.START).build()
                ))
                .coordinate(CoordinateResponse.from(Coordinate.builder().x(1.0F).y(1.0F).build()))
                .build();


        given(nodeService.getNode(any(User.class), anyLong()))
                .willReturn(answerNodeDetailResponse);

        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/nodes/{nodeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-node",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Node")
                                .summary("노드 상세 조회")
                                .description("노드별 스키마 노션 API 명세서에 제공")
                                .pathParameters(
                                        parameterWithName("nodeId").description("노드 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.nodeId").type(JsonFieldType.NUMBER)
                                                .description("노드 아이디"),
                                        fieldWithPath("data.type").type(JsonFieldType.STRING)
                                                .description("노드 타입"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                                                .description("노드 이름"),
                                        fieldWithPath("data.coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("노드 X좌표"),
                                        fieldWithPath("data.coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("노드 Y좌표"),
                                        fieldWithPath("data.inputEdges[].edgeId").type(JsonFieldType.NUMBER)
                                                .description("진입 간선의 아이디"),
                                        fieldWithPath("data.inputEdges[].sourceNodeId").type(JsonFieldType.NUMBER)
                                                .description("진입 간선의 출처 노드 아이디"),
                                        fieldWithPath("data.inputEdges[].targetNodeId").type(JsonFieldType.NUMBER)
                                                .description("진입 간선의 목표 노드 아이디"),
                                        fieldWithPath("data.inputEdges[].sourceConditionId").type(JsonFieldType.NULL)
                                                .description("진입 간선의 출처 노드 추가정보"),
                                        fieldWithPath("data.outputEdges").type(JsonFieldType.NULL)
                                                .description("진출 간선들의 리스트"),
                                        fieldWithPath("data.precedingNodes[].nodeId").type(JsonFieldType.NUMBER)
                                                .description("선행 노드의 아이디"),
                                        fieldWithPath("data.precedingNodes[].name").type(JsonFieldType.STRING)
                                                .description("선행 노드들의 이름"),
                                        fieldWithPath("data.precedingNodes[].type").type(JsonFieldType.STRING)
                                                .description("선행 노드들의 노드 타입"),
                                        fieldWithPath("data.outputMessage").type(JsonFieldType.STRING)
                                                .description("Answer 노드의 내용")
                                )
                                .build())));


    }
}
