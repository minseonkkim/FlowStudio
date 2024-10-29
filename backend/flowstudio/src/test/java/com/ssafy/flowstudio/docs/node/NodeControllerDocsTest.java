package com.ssafy.flowstudio.docs.node;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ssafy.flowstudio.api.controller.node.NodeController;
import com.ssafy.flowstudio.api.controller.node.request.CoordinateRequest;
import com.ssafy.flowstudio.api.controller.node.request.NodeCreateRequest;
import com.ssafy.flowstudio.api.service.node.NodeService;
import com.ssafy.flowstudio.api.service.node.request.NodeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.node.response.NodeCreateResponse;
import com.ssafy.flowstudio.docs.RestDocsSupport;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
                .nodeType(NodeType.START)
                .build();

        NodeCreateResponse response = NodeCreateResponse.builder()
                .nodeId(1L)
                .nodeType(NodeType.START)
                .build();

        given(nodeService.createNode(any(User.class), any(NodeCreateServiceRequest.class)))
                .willReturn(response);

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
                                        fieldWithPath("nodeType").type(JsonFieldType.STRING)
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
                                        fieldWithPath("data.nodeType").type(JsonFieldType.STRING)
                                                .description("노드 타입")
                                )
                                .build())));

    }
}
