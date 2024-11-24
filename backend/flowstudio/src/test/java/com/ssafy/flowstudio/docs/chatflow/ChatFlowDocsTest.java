package com.ssafy.flowstudio.docs.chatflow;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ssafy.flowstudio.api.controller.chatflow.ChatFlowController;
import com.ssafy.flowstudio.api.controller.chatflow.request.ChatFlowRequest;
import com.ssafy.flowstudio.api.service.chatflow.ChatFlowService;
import com.ssafy.flowstudio.api.service.chatflow.request.ChatFlowServiceRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.*;
import com.ssafy.flowstudio.api.service.node.response.AnswerResponse;
import com.ssafy.flowstudio.api.service.node.response.LlmResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.QuestionClassifierResponse;
import com.ssafy.flowstudio.api.service.node.response.RetrieverResponse;
import com.ssafy.flowstudio.api.service.node.response.StartResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeResponse;
import com.ssafy.flowstudio.api.service.user.response.UserResponse;
import com.ssafy.flowstudio.docs.RestDocsSupport;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatFlowDocsTest extends RestDocsSupport {

    private ChatFlowService chatFlowService = mock(ChatFlowService.class);

    @Override
    protected Object initController() {
        return new ChatFlowController(chatFlowService);
    }

    @DisplayName("챗플로우 목록을 조회한다")
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
                .shareCount(0)
                .build();

        given(chatFlowService.getChatFlows(any(User.class), anyBoolean(), anyBoolean(), anyInt(), anyInt()))
                .willReturn(List.of(response));

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows")
                        .param("isShared", "false")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-chatflow-list",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("챗플로우 목록 조회")
                                .queryParameters(
                                        parameterWithName("isShared").optional().description("공유여부"),
                                        parameterWithName("test").optional().description("테스트 존재 여부"),
                                        parameterWithName("page").optional().description("조회할 페이지, 입력 없으면 default 0"),
                                        parameterWithName("size").optional().description("사이즈, 입력 없으면 default 20")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data[].chatFlowId").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 아이디"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING)
                                                .description("챗플로우 제목"),
                                        fieldWithPath("data[].description").type(JsonFieldType.STRING)
                                                .description("챗플로우 설명"),
                                        fieldWithPath("data[].thumbnail").type(JsonFieldType.STRING)
                                                .description("챗플로우 썸네일"),
                                        fieldWithPath("data[].public").type(JsonFieldType.BOOLEAN)
                                                .description("챗플로우 공유 여부"),
                                        fieldWithPath("data[].author.id").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 작성자 아이디"),
                                        fieldWithPath("data[].author.id").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 작성자 아이디"),
                                        fieldWithPath("data[].author.username").type(JsonFieldType.STRING)
                                                .description("챗플로우 작성자 이메일"),
                                        fieldWithPath("data[].author.nickname").type(JsonFieldType.STRING)
                                                .description("챗플로우 작성자 닉네임"),
                                        fieldWithPath("data[].author.profileImage").type(JsonFieldType.STRING)
                                                .description("챗플로우 작성자 프로필이미지"),
                                        fieldWithPath("data[].shareCount").type(JsonFieldType.NUMBER)
                                                .description("챗플로우가 공유된 횟수"),
                                        fieldWithPath("data[].categories[].categoryId").type(JsonFieldType.NUMBER)
                                                .description("카테고리 아이디"),
                                        fieldWithPath("data[].categories[].name").type(JsonFieldType.STRING)
                                                .description("카테고리 이름"))
                                .build())));

    }

    @DisplayName("모두의 챗플로우 목록을 조회한다")
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
                .shareCount(0)
                .build();

        given(chatFlowService.getEveryoneChatFlows(0, 20))
                .willReturn(List.of(response));

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/shares")
                        .param("page", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-everyone-chatflow-list",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("모두의 챗플로우 목록 조회")
                                .queryParameters(
                                        parameterWithName("page").optional().description("조회할 페이지, 입력 없으면 default 0"),
                                        parameterWithName("size").optional().description("사이즈, 입력 없으면 default 20")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data[].chatFlowId").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 아이디"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING)
                                                .description("챗플로우 제목"),
                                        fieldWithPath("data[].description").type(JsonFieldType.STRING)
                                                .description("챗플로우 설명"),
                                        fieldWithPath("data[].thumbnail").type(JsonFieldType.STRING)
                                                .description("챗플로우 썸네일"),
                                        fieldWithPath("data[].public").type(JsonFieldType.BOOLEAN)
                                                .description("챗플로우 공유 여부"),
                                        fieldWithPath("data[].author.id").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 작성자 아이디"),
                                        fieldWithPath("data[].author.id").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 작성자 아이디"),
                                        fieldWithPath("data[].author.username").type(JsonFieldType.STRING)
                                                .description("챗플로우 작성자 이메일"),
                                        fieldWithPath("data[].author.nickname").type(JsonFieldType.STRING)
                                                .description("챗플로우 작성자 닉네임"),
                                        fieldWithPath("data[].author.profileImage").type(JsonFieldType.STRING)
                                                .description("챗플로우 작성자 프로필이미지"),
                                        fieldWithPath("data[].shareCount").type(JsonFieldType.NUMBER)
                                                .description("챗플로우가 공유된 횟수"),
                                        fieldWithPath("data[].categories[].categoryId").type(JsonFieldType.NUMBER)
                                                .description("카테고리 아이디"),
                                        fieldWithPath("data[].categories[].name").type(JsonFieldType.STRING)
                                                .description("카테고리 이름"))
                                .build())));

    }

    @DisplayName("챗플로우를 조회한다")
    @Test
    void getChatFlow() throws Exception {
        // given
        EdgeResponse edge1 = EdgeResponse.builder()
                .edgeId(1L)
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .build();

        EdgeResponse edge2 = EdgeResponse.builder()
                .edgeId(2L)
                .sourceNodeId(2L)
                .targetNodeId(3L)
                .sourceConditionId(1L)
                .build();

        EdgeResponse edge3 = EdgeResponse.builder()
                .edgeId(3L)
                .sourceNodeId(3L)
                .targetNodeId(4L)
                .build();

        EdgeResponse edge4 = EdgeResponse.builder()
                .edgeId(4L)
                .sourceNodeId(4L)
                .targetNodeId(5L)
                .build();

        CoordinateResponse coordinate = CoordinateResponse.builder()
                .x(100)
                .y(100)
                .build();

        NodeResponse node1 = StartResponse.builder()
                .nodeId(1L)
                .name("Start")
                .type(NodeType.START)
                .coordinate(coordinate)
                .inputEdges(List.of(edge1))
                .outputEdges(List.of())
                .maxLength(10)
                .build();

        NodeResponse node2 = QuestionClassifierResponse.builder()
                .nodeId(2L)
                .name("QuestionClassifier")
                .type(NodeType.QUESTION_CLASSIFIER)
                .coordinate(coordinate)
                .inputEdges(List.of(edge1))
                .outputEdges(List.of(edge2))
                .build();

        KnowledgeResponse knowledge = KnowledgeResponse.builder()
                .knowledgeId(1L)
                .title("title")
                .isPublic(true)
                .createdAt(LocalDateTime.of(2021, 1, 1, 0, 0))
                .totalToken(10)
                .build();

        NodeResponse node3 = RetrieverResponse.builder()
                .nodeId(3L)
                .name("Retriever")
                .type(NodeType.RETRIEVER)
                .coordinate(coordinate)
                .knowledge(knowledge)
                .intervalTime(10)
                .topK(10)
                .scoreThreshold(0.5f)
                .query("query")
                .inputEdges(List.of(edge2))
                .outputEdges(List.of(edge3))
                .build();

        NodeResponse node4 = LlmResponse.builder()
                .nodeId(4L)
                .name("LLM")
                .type(NodeType.LLM)
                .promptSystem("promptSystem")
                .promptUser("promptUser")
                .coordinate(coordinate)
                .inputEdges(List.of(edge3))
                .outputEdges(List.of(edge4))
                .build();

        NodeResponse node5 = AnswerResponse.builder()
                .nodeId(5L)
                .name("Answer")
                .type(NodeType.ANSWER)
                .coordinate(coordinate)
                .outputEdges(List.of())
                .inputEdges(List.of(edge4))
                .outputMessage("outputMessage")
                .build();

        ChatFlowResponse response = ChatFlowResponse.builder()
                .chatFlowId(1L)
                .title("title")
                .nodes(List.of(node1, node2, node3, node4, node5))
                .edges(List.of(edge1, edge2, edge3, edge4))
                .publishUrl("uuid")
                .publishedAt(LocalDateTime.now())
                .build();

        given(chatFlowService.getChatFlow(any(User.class), any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/{chatFlowId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-chatflow-detail",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("챗플로우 상세 조회")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.chatFlowId").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 아이디"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING)
                                                .description("챗플로우 제목"),
                                        fieldWithPath("data.publishUrl").type(JsonFieldType.STRING)
                                                .description("챗플로우 주소"),
                                        fieldWithPath("data.publishedAt").type(JsonFieldType.STRING)
                                                .description("챗플로우 발행날짜"),
                                        fieldWithPath("data.nodes[].nodeId").type(JsonFieldType.NUMBER)
                                                .description("노드 아이디"),
                                        fieldWithPath("data.nodes[].name").type(JsonFieldType.STRING)
                                                .description("노드 이름"),
                                        fieldWithPath("data.nodes[].type").type(JsonFieldType.STRING)
                                                .description("노드 타입"),
                                        fieldWithPath("data.nodes[].questionClasses").optional().type(JsonFieldType.STRING)
                                                .description("노드 타입"),
                                        fieldWithPath("data.nodes[].intervalTime").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 intervalTime"),
                                        fieldWithPath("data.nodes[].topK").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 topK"),
                                        fieldWithPath("data.nodes[].scoreThreshold").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 scoreThreshold"),
                                        fieldWithPath("data.nodes[].query").optional().type(JsonFieldType.STRING)
                                                .description("Retriever노드 query"),
                                        fieldWithPath("data.nodes[].knowledge.knowledgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 지식 아이디"),
                                        fieldWithPath("data.nodes[].knowledge.title").optional().type(JsonFieldType.STRING)
                                                .description("Retriever노드 지식 제목"),
                                        fieldWithPath("data.nodes[].knowledge.isPublic").optional().type(JsonFieldType.BOOLEAN)
                                                .description("Retriever노드 지식 공개여부"),
                                        fieldWithPath("data.nodes[].knowledge.createdAt").optional().type(JsonFieldType.STRING)
                                                .description("Retriever노드 지식 생성일"),
                                        fieldWithPath("data.nodes[].knowledge.totalToken").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 지식 토큰 수"),
                                        fieldWithPath("data.nodes[].promptSystem").optional().type(JsonFieldType.STRING)
                                                .description("LLM노드 시스템프롬프트"),
                                        fieldWithPath("data.nodes[].promptUser").optional().type(JsonFieldType.STRING)
                                                .description("LLM노드 유저프롬프트"),
                                        fieldWithPath("data.nodes[].outputMessage").optional().type(JsonFieldType.STRING)
                                                .description("Answer노드 출력메시지"),
                                        fieldWithPath("data.nodes[].maxLength").optional().type(JsonFieldType.NUMBER)
                                                .description("Start 노드 입력 최대길이"),
                                        fieldWithPath("data.nodes[].coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("노드 X좌표"),
                                        fieldWithPath("data.nodes[].coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("노드 Y좌표"),
                                        fieldWithPath("data.nodes[].outputEdges[].edgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("output 간선 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("output 간선"),
                                        fieldWithPath("data.nodes[].outputEdges[].sourceNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("output 간선 출발노드 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges[].targetNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("output 간선 도착노드 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges[].sourceConditionId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 시작 조건 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].edgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].sourceNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 출발노드 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].targetNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 도착노드 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].sourceConditionId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 시작 조건 아이디"),
                                        fieldWithPath("data.edges[].edgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("edge 아이디"),
                                        fieldWithPath("data.edges[].sourceNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("시작 노드"),
                                        fieldWithPath("data.edges[].targetNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("도착 노드"),
                                        fieldWithPath("data.edges[].sourceConditionId").optional().type(JsonFieldType.NUMBER)
                                                .description("조건 아이디")
                                )
                                .build()
                        )
                ));

    }

    @DisplayName("챗플로우를 생성한다")
    @Test
    void createChatFlow() throws Exception {
        // given
        ChatFlowRequest request = ChatFlowRequest.builder()
                .title("title")
                .thumbnail("thumbnail")
                .description("description")
                .categoryIds(List.of(1L, 2L))
                .build();

        CoordinateResponse coordinate = CoordinateResponse.builder()
                .x(100)
                .y(100)
                .build();

        NodeResponse node1 = StartResponse.builder()
                .nodeId(1L)
                .name("Start")
                .type(NodeType.START)
                .coordinate(coordinate)
                .inputEdges(List.of())
                .outputEdges(List.of())
                .maxLength(10)
                .build();

        ChatFlowResponse response = ChatFlowResponse.builder()
                .chatFlowId(1L)
                .title("title")
                .nodes(List.of(node1))
                .edges(null)
                .publishUrl(null)
                .publishedAt(null)
                .build();

        given(chatFlowService.createChatFlow(any(User.class), any(ChatFlowServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("create-chatflow",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("챗플로우 생성")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.chatFlowId").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 아이디"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING)
                                                .description("챗플로우 제목"),
                                        fieldWithPath("data.publishUrl").type(JsonFieldType.NULL)
                                                .description("챗플로우 주소"),
                                        fieldWithPath("data.publishedAt").type(JsonFieldType.NULL)
                                                .description("챗플로우 발행날짜"),
                                        fieldWithPath("data.nodes[].nodeId").type(JsonFieldType.NUMBER)
                                                .description("노드 아이디"),
                                        fieldWithPath("data.nodes[].name").type(JsonFieldType.STRING)
                                                .description("노드 이름"),
                                        fieldWithPath("data.nodes[].type").type(JsonFieldType.STRING)
                                                .description("노드 타입"),
                                        fieldWithPath("data.nodes[].maxLength").optional().type(JsonFieldType.NUMBER)
                                                .description("Start 노드 입력 최대길이"),
                                        fieldWithPath("data.nodes[].coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("노드 X좌표"),
                                        fieldWithPath("data.nodes[].coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("노드 Y좌표"),
                                        fieldWithPath("data.nodes[].inputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("input 간선 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("output 간선"),
                                        fieldWithPath("data.edges").optional().type(JsonFieldType.NULL)
                                                .description("edge 들")
                                )
                                .build()
                        )
                ));
    }

    @DisplayName("챗플로우를 수정한다")
    @Test
    void updateChatFlow() throws Exception {
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
                .categories(List.of(CategoryResponse.builder()
                        .categoryId(1L)
                        .name("카테고리1")
                        .build(), CategoryResponse.builder()
                        .categoryId(2L)
                        .name("카테고리2")
                        .build()))
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
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-chatflow",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("챗플로우 수정")
                                .pathParameters(
                                        parameterWithName("chatFlowId").description("챗플로우 아이디")
                                )
                                .requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING)
                                                .description("챗플로우 제목"),
                                        fieldWithPath("thumbnail").type(JsonFieldType.STRING)
                                                .description("챗플로우 썸네일"),
                                        fieldWithPath("description").type(JsonFieldType.STRING)
                                                .description("챗플로우 설명"),
                                        fieldWithPath("categoryIds").type(JsonFieldType.ARRAY)
                                                .description("카테고리 아이디 리스트")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.chatFlowId").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 아이디"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING)
                                                .description("챗플로우 제목"),
                                        fieldWithPath("data.description").type(JsonFieldType.STRING)
                                                .description("챗플로우 설명"),
                                        fieldWithPath("data.thumbnail").type(JsonFieldType.STRING)
                                                .description("챗플로우 썸네일"),
                                        fieldWithPath("data.categories[].categoryId").type(JsonFieldType.NUMBER)
                                                .description("카테고리 아이디"),
                                        fieldWithPath("data.categories[].name").type(JsonFieldType.STRING)
                                                .description("카테고리 이름")
                                )
                                .build())));
    }

    @DisplayName("챗플로우를 삭제한다")
    @Test
    void deleteChatFlow() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                delete("/api/v1/chat-flows/{chatFlowId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON));
        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-chatflow",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("챗플로우 삭제")
                                .pathParameters(
                                        parameterWithName("chatFlowId").description("챗플로우 아이디")
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


    @DisplayName("예시 챗플로우를 생성한다")
    @Test
    void createExampleChatFlow() throws Exception {
        EdgeResponse edge1 = EdgeResponse.builder()
                .edgeId(1L)
                .sourceNodeId(1L)
                .targetNodeId(2L)
                .build();

        EdgeResponse edge2 = EdgeResponse.builder()
                .edgeId(2L)
                .sourceNodeId(2L)
                .targetNodeId(3L)
                .sourceConditionId(1L)
                .build();

        EdgeResponse edge3 = EdgeResponse.builder()
                .edgeId(3L)
                .sourceNodeId(3L)
                .targetNodeId(4L)
                .build();

        EdgeResponse edge4 = EdgeResponse.builder()
                .edgeId(4L)
                .sourceNodeId(4L)
                .targetNodeId(5L)
                .build();

        CoordinateResponse coordinate = CoordinateResponse.builder()
                .x(100)
                .y(100)
                .build();

        NodeResponse node1 = StartResponse.builder()
                .nodeId(1L)
                .name("Start")
                .type(NodeType.START)
                .coordinate(coordinate)
                .inputEdges(List.of(edge1))
                .outputEdges(List.of())
                .maxLength(10)
                .build();

        NodeResponse node2 = QuestionClassifierResponse.builder()
                .nodeId(2L)
                .name("QuestionClassifier")
                .type(NodeType.QUESTION_CLASSIFIER)
                .coordinate(coordinate)
                .inputEdges(List.of(edge1))
                .outputEdges(List.of(edge2))
                .build();

        KnowledgeResponse knowledge = KnowledgeResponse.builder()
                .knowledgeId(1L)
                .title("title")
                .isPublic(true)
                .createdAt(LocalDateTime.of(2021, 1, 1, 0, 0))
                .totalToken(10)
                .build();

        NodeResponse node3 = RetrieverResponse.builder()
                .nodeId(3L)
                .name("Retriever")
                .type(NodeType.RETRIEVER)
                .coordinate(coordinate)
                .knowledge(knowledge)
                .intervalTime(10)
                .topK(10)
                .scoreThreshold(0.5f)
                .query("query")
                .inputEdges(List.of(edge2))
                .outputEdges(List.of(edge3))
                .build();

        NodeResponse node4 = LlmResponse.builder()
                .nodeId(4L)
                .name("LLM")
                .type(NodeType.LLM)
                .promptSystem("promptSystem")
                .promptUser("promptUser")
                .coordinate(coordinate)
                .inputEdges(List.of(edge3))
                .outputEdges(List.of(edge4))
                .build();

        NodeResponse node5 = AnswerResponse.builder()
                .nodeId(5L)
                .name("Answer")
                .type(NodeType.ANSWER)
                .coordinate(coordinate)
                .outputEdges(List.of())
                .inputEdges(List.of(edge4))
                .outputMessage("outputMessage")
                .build();

        ChatFlowResponse response = ChatFlowResponse.builder()
                .chatFlowId(1L)
                .title("title")
                .nodes(List.of(node1, node2, node3, node4, node5))
                .edges(List.of(edge1, edge2, edge3, edge4))
                .publishUrl(null)
                .publishedAt(null)
                .build();

        given(chatFlowService.createExampleChatFlow(any(User.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/example", 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("create-chatflow-example",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("챗플로우 예시 생성")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.chatFlowId").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 아이디"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING)
                                                .description("챗플로우 제목"),
                                        fieldWithPath("data.publishUrl").type(JsonFieldType.NULL)
                                                .description("챗플로우 주소"),
                                        fieldWithPath("data.publishedAt").type(JsonFieldType.NULL)
                                                .description("챗플로우 발행날짜"),
                                        fieldWithPath("data.nodes[].nodeId").type(JsonFieldType.NUMBER)
                                                .description("노드 아이디"),
                                        fieldWithPath("data.nodes[].name").type(JsonFieldType.STRING)
                                                .description("노드 이름"),
                                        fieldWithPath("data.nodes[].type").type(JsonFieldType.STRING)
                                                .description("노드 타입"),
                                        fieldWithPath("data.nodes[].questionClasses").optional().type(JsonFieldType.STRING)
                                                .description("노드 타입"),
                                        fieldWithPath("data.nodes[].intervalTime").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 intervalTime"),
                                        fieldWithPath("data.nodes[].topK").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 topK"),
                                        fieldWithPath("data.nodes[].scoreThreshold").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 scoreThreshold"),
                                        fieldWithPath("data.nodes[].query").optional().type(JsonFieldType.STRING)
                                                .description("Retriever노드 query"),
                                        fieldWithPath("data.nodes[].knowledge.knowledgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 지식 아이디"),
                                        fieldWithPath("data.nodes[].knowledge.title").optional().type(JsonFieldType.STRING)
                                                .description("Retriever노드 지식 제목"),
                                        fieldWithPath("data.nodes[].knowledge.isPublic").optional().type(JsonFieldType.BOOLEAN)
                                                .description("Retriever노드 지식 공개여부"),
                                        fieldWithPath("data.nodes[].knowledge.createdAt").optional().type(JsonFieldType.STRING)
                                                .description("Retriever노드 지식 생성일"),
                                        fieldWithPath("data.nodes[].knowledge.totalToken").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 지식 토큰 수"),
                                        fieldWithPath("data.nodes[].promptSystem").optional().type(JsonFieldType.STRING)
                                                .description("LLM노드 시스템프롬프트"),
                                        fieldWithPath("data.nodes[].promptUser").optional().type(JsonFieldType.STRING)
                                                .description("LLM노드 유저프롬프트"),
                                        fieldWithPath("data.nodes[].outputMessage").optional().type(JsonFieldType.STRING)
                                                .description("Answer노드 출력메시지"),
                                        fieldWithPath("data.nodes[].maxLength").optional().type(JsonFieldType.NUMBER)
                                                .description("Start 노드 입력 최대길이"),
                                        fieldWithPath("data.nodes[].coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("노드 X좌표"),
                                        fieldWithPath("data.nodes[].coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("노드 Y좌표"),
                                        fieldWithPath("data.nodes[].outputEdges[].edgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("output 간선 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("output 간선"),
                                        fieldWithPath("data.nodes[].outputEdges[].sourceNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("output 간선 출발노드 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges[].targetNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("output 간선 도착노드 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges[].sourceConditionId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 시작 조건 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].edgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].sourceNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 출발노드 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].targetNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 도착노드 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].sourceConditionId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 시작 조건 아이디"),
                                        fieldWithPath("data.edges[].edgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("edge 아이디"),
                                        fieldWithPath("data.edges[].sourceNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("시작 노드"),
                                        fieldWithPath("data.edges[].targetNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("도착 노드"),
                                        fieldWithPath("data.edges[].sourceConditionId").optional().type(JsonFieldType.NUMBER)
                                                .description("조건 아이디")
                                )
                                .build()
                        )
                ));
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
                .edgeId(2L)
                .sourceNodeId(2L)
                .targetNodeId(3L)
                .sourceConditionId(1L)
                .build();

        EdgeResponse edge3 = EdgeResponse.builder()
                .edgeId(3L)
                .sourceNodeId(3L)
                .targetNodeId(4L)
                .build();

        EdgeResponse edge4 = EdgeResponse.builder()
                .edgeId(4L)
                .sourceNodeId(4L)
                .targetNodeId(5L)
                .build();

        CoordinateResponse coordinate = CoordinateResponse.builder()
                .x(100)
                .y(100)
                .build();

        NodeResponse node1 = StartResponse.builder()
                .nodeId(1L)
                .name("Start")
                .type(NodeType.START)
                .coordinate(coordinate)
                .inputEdges(List.of(edge1))
                .outputEdges(List.of())
                .maxLength(10)
                .build();

        NodeResponse node2 = QuestionClassifierResponse.builder()
                .nodeId(2L)
                .name("QuestionClassifier")
                .type(NodeType.QUESTION_CLASSIFIER)
                .coordinate(coordinate)
                .inputEdges(List.of(edge1))
                .outputEdges(List.of(edge2))
                .build();

        KnowledgeResponse knowledge = KnowledgeResponse.builder()
                .knowledgeId(1L)
                .title("title")
                .isPublic(true)
                .createdAt(LocalDateTime.of(2021, 1, 1, 0, 0))
                .totalToken(10)
                .build();

        NodeResponse node3 = RetrieverResponse.builder()
                .nodeId(3L)
                .name("Retriever")
                .type(NodeType.RETRIEVER)
                .coordinate(coordinate)
                .knowledge(knowledge)
                .intervalTime(10)
                .topK(10)
                .scoreThreshold(0.5f)
                .query("query")
                .inputEdges(List.of(edge2))
                .outputEdges(List.of(edge3))
                .build();

        NodeResponse node4 = LlmResponse.builder()
                .nodeId(4L)
                .name("LLM")
                .type(NodeType.LLM)
                .promptSystem("promptSystem")
                .promptUser("promptUser")
                .coordinate(coordinate)
                .inputEdges(List.of(edge3))
                .outputEdges(List.of(edge4))
                .build();

        NodeResponse node5 = AnswerResponse.builder()
                .nodeId(5L)
                .name("Answer")
                .type(NodeType.ANSWER)
                .coordinate(coordinate)
                .outputEdges(List.of())
                .inputEdges(List.of(edge4))
                .outputMessage("outputMessage")
                .build();

        ChatFlowResponse response = ChatFlowResponse.builder()
                .chatFlowId(1L)
                .title("title")
                .nodes(List.of(node1, node2, node3, node4, node5))
                .edges(List.of(edge1, edge2, edge3, edge4))
                .build();

        given(chatFlowService.uploadChatFlow(any(User.class), any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/{chatFlowId}/upload", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("upload-chatflow",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("챗플로우 업로드")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.chatFlowId").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 아이디"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING)
                                                .description("챗플로우 제목"),
                                        fieldWithPath("data.publishUrl").type(JsonFieldType.NULL)
                                                .description("챗플로우 주소"),
                                        fieldWithPath("data.publishedAt").type(JsonFieldType.NULL)
                                                .description("챗플로우 발행날짜"),
                                        fieldWithPath("data.nodes[].nodeId").type(JsonFieldType.NUMBER)
                                                .description("노드 아이디"),
                                        fieldWithPath("data.nodes[].name").type(JsonFieldType.STRING)
                                                .description("노드 이름"),
                                        fieldWithPath("data.nodes[].type").type(JsonFieldType.STRING)
                                                .description("노드 타입"),
                                        fieldWithPath("data.nodes[].questionClasses").optional().type(JsonFieldType.STRING)
                                                .description("노드 타입"),
                                        fieldWithPath("data.nodes[].intervalTime").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 intervalTime"),
                                        fieldWithPath("data.nodes[].topK").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 topK"),
                                        fieldWithPath("data.nodes[].scoreThreshold").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 scoreThreshold"),
                                        fieldWithPath("data.nodes[].query").optional().type(JsonFieldType.STRING)
                                                .description("Retriever노드 query"),
                                        fieldWithPath("data.nodes[].knowledge.knowledgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 지식 아이디"),
                                        fieldWithPath("data.nodes[].knowledge.title").optional().type(JsonFieldType.STRING)
                                                .description("Retriever노드 지식 제목"),
                                        fieldWithPath("data.nodes[].knowledge.isPublic").optional().type(JsonFieldType.BOOLEAN)
                                                .description("Retriever노드 지식 공개여부"),
                                        fieldWithPath("data.nodes[].knowledge.createdAt").optional().type(JsonFieldType.STRING)
                                                .description("Retriever노드 지식 생성일"),
                                        fieldWithPath("data.nodes[].knowledge.totalToken").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 지식 토큰 수"),
                                        fieldWithPath("data.nodes[].promptSystem").optional().type(JsonFieldType.STRING)
                                                .description("LLM노드 시스템프롬프트"),
                                        fieldWithPath("data.nodes[].promptUser").optional().type(JsonFieldType.STRING)
                                                .description("LLM노드 유저프롬프트"),
                                        fieldWithPath("data.nodes[].outputMessage").optional().type(JsonFieldType.STRING)
                                                .description("Answer노드 출력메시지"),
                                        fieldWithPath("data.nodes[].maxLength").optional().type(JsonFieldType.NUMBER)
                                                .description("Start 노드 입력 최대길이"),
                                        fieldWithPath("data.nodes[].coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("노드 X좌표"),
                                        fieldWithPath("data.nodes[].coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("노드 Y좌표"),
                                        fieldWithPath("data.nodes[].outputEdges[].edgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("output 간선 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("output 간선"),
                                        fieldWithPath("data.nodes[].outputEdges[].sourceNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("output 간선 출발노드 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges[].targetNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("output 간선 도착노드 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges[].sourceConditionId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 시작 조건 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].edgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].sourceNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 출발노드 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].targetNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 도착노드 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].sourceConditionId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 시작 조건 아이디"),
                                        fieldWithPath("data.edges[].edgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("edge 아이디"),
                                        fieldWithPath("data.edges[].sourceNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("시작 노드"),
                                        fieldWithPath("data.edges[].targetNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("도착 노드"),
                                        fieldWithPath("data.edges[].sourceConditionId").optional().type(JsonFieldType.NUMBER)
                                                .description("조건 아이디")
                                )
                                .build()
                        )
                ));
    }

    @DisplayName("챗플로우를 업로드한다.")
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
                .edgeId(2L)
                .sourceNodeId(2L)
                .targetNodeId(3L)
                .sourceConditionId(1L)
                .build();

        EdgeResponse edge3 = EdgeResponse.builder()
                .edgeId(3L)
                .sourceNodeId(3L)
                .targetNodeId(4L)
                .build();

        EdgeResponse edge4 = EdgeResponse.builder()
                .edgeId(4L)
                .sourceNodeId(4L)
                .targetNodeId(5L)
                .build();

        CoordinateResponse coordinate = CoordinateResponse.builder()
                .x(100)
                .y(100)
                .build();

        NodeResponse node1 = StartResponse.builder()
                .nodeId(1L)
                .name("Start")
                .type(NodeType.START)
                .coordinate(coordinate)
                .inputEdges(List.of(edge1))
                .outputEdges(List.of())
                .maxLength(10)
                .build();

        NodeResponse node2 = QuestionClassifierResponse.builder()
                .nodeId(2L)
                .name("QuestionClassifier")
                .type(NodeType.QUESTION_CLASSIFIER)
                .coordinate(coordinate)
                .inputEdges(List.of(edge1))
                .outputEdges(List.of(edge2))
                .build();

        KnowledgeResponse knowledge = KnowledgeResponse.builder()
                .knowledgeId(1L)
                .title("title")
                .isPublic(true)
                .createdAt(LocalDateTime.of(2021, 1, 1, 0, 0))
                .totalToken(10)
                .build();

        NodeResponse node3 = RetrieverResponse.builder()
                .nodeId(3L)
                .name("Retriever")
                .type(NodeType.RETRIEVER)
                .coordinate(coordinate)
                .knowledge(knowledge)
                .intervalTime(10)
                .topK(10)
                .scoreThreshold(0.5f)
                .query("query")
                .inputEdges(List.of(edge2))
                .outputEdges(List.of(edge3))
                .build();

        NodeResponse node4 = LlmResponse.builder()
                .nodeId(4L)
                .name("LLM")
                .type(NodeType.LLM)
                .promptSystem("promptSystem")
                .promptUser("promptUser")
                .coordinate(coordinate)
                .inputEdges(List.of(edge3))
                .outputEdges(List.of(edge4))
                .build();

        NodeResponse node5 = AnswerResponse.builder()
                .nodeId(5L)
                .name("Answer")
                .type(NodeType.ANSWER)
                .coordinate(coordinate)
                .outputEdges(List.of())
                .inputEdges(List.of(edge4))
                .outputMessage("outputMessage")
                .build();

        ChatFlowResponse response = ChatFlowResponse.builder()
                .chatFlowId(1L)
                .title("title")
                .nodes(List.of(node1, node2, node3, node4, node5))
                .edges(List.of(edge1, edge2, edge3, edge4))
                .build();

        given(chatFlowService.downloadChatFlow(any(User.class), any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/{chatFlowId}/download", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("download-chatflow",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("챗플로우 다운로드")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.chatFlowId").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 아이디"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING)
                                                .description("챗플로우 제목"),
                                        fieldWithPath("data.publishUrl").type(JsonFieldType.NULL)
                                                .description("챗플로우 주소"),
                                        fieldWithPath("data.publishedAt").type(JsonFieldType.NULL)
                                                .description("챗플로우 발행날짜"),
                                        fieldWithPath("data.nodes[].nodeId").type(JsonFieldType.NUMBER)
                                                .description("노드 아이디"),
                                        fieldWithPath("data.nodes[].name").type(JsonFieldType.STRING)
                                                .description("노드 이름"),
                                        fieldWithPath("data.nodes[].type").type(JsonFieldType.STRING)
                                                .description("노드 타입"),
                                        fieldWithPath("data.nodes[].questionClasses").optional().type(JsonFieldType.STRING)
                                                .description("노드 타입"),
                                        fieldWithPath("data.nodes[].intervalTime").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 intervalTime"),
                                        fieldWithPath("data.nodes[].topK").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 topK"),
                                        fieldWithPath("data.nodes[].scoreThreshold").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 scoreThreshold"),
                                        fieldWithPath("data.nodes[].query").optional().type(JsonFieldType.STRING)
                                                .description("Retriever노드 query"),
                                        fieldWithPath("data.nodes[].knowledge.knowledgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 지식 아이디"),
                                        fieldWithPath("data.nodes[].knowledge.title").optional().type(JsonFieldType.STRING)
                                                .description("Retriever노드 지식 제목"),
                                        fieldWithPath("data.nodes[].knowledge.isPublic").optional().type(JsonFieldType.BOOLEAN)
                                                .description("Retriever노드 지식 공개여부"),
                                        fieldWithPath("data.nodes[].knowledge.createdAt").optional().type(JsonFieldType.STRING)
                                                .description("Retriever노드 지식 생성일"),
                                        fieldWithPath("data.nodes[].knowledge.totalToken").optional().type(JsonFieldType.NUMBER)
                                                .description("Retriever노드 지식 토큰 수"),
                                        fieldWithPath("data.nodes[].promptSystem").optional().type(JsonFieldType.STRING)
                                                .description("LLM노드 시스템프롬프트"),
                                        fieldWithPath("data.nodes[].promptUser").optional().type(JsonFieldType.STRING)
                                                .description("LLM노드 유저프롬프트"),
                                        fieldWithPath("data.nodes[].outputMessage").optional().type(JsonFieldType.STRING)
                                                .description("Answer노드 출력메시지"),
                                        fieldWithPath("data.nodes[].maxLength").optional().type(JsonFieldType.NUMBER)
                                                .description("Start 노드 입력 최대길이"),
                                        fieldWithPath("data.nodes[].coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("노드 X좌표"),
                                        fieldWithPath("data.nodes[].coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("노드 Y좌표"),
                                        fieldWithPath("data.nodes[].outputEdges[].edgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("output 간선 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("output 간선"),
                                        fieldWithPath("data.nodes[].outputEdges[].sourceNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("output 간선 출발노드 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges[].targetNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("output 간선 도착노드 아이디"),
                                        fieldWithPath("data.nodes[].outputEdges[].sourceConditionId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 시작 조건 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].edgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].sourceNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 출발노드 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].targetNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 도착노드 아이디"),
                                        fieldWithPath("data.nodes[].inputEdges[].sourceConditionId").optional().type(JsonFieldType.NUMBER)
                                                .description("input 간선 시작 조건 아이디"),
                                        fieldWithPath("data.edges[].edgeId").optional().type(JsonFieldType.NUMBER)
                                                .description("edge 아이디"),
                                        fieldWithPath("data.edges[].sourceNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("시작 노드"),
                                        fieldWithPath("data.edges[].targetNodeId").optional().type(JsonFieldType.NUMBER)
                                                .description("도착 노드"),
                                        fieldWithPath("data.edges[].sourceConditionId").optional().type(JsonFieldType.NUMBER)
                                                .description("조건 아이디")
                                )
                                .build()
                        )
                ));
    }

    @DisplayName("카테고리 목록을 조회한다.")
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
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-categories",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("챗플로우 카테고리 목록 조회")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data[].categoryId").type(JsonFieldType.NUMBER)
                                                .description("카테고리 아이디"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING)
                                                .description("카테고리 이름")
                                )
                                .build())));
    }

    @DisplayName("챗플로우 실행 가능 여부를 사전 점검한다.")
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
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("precheck-chatflow",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("챗플로우 실행여부 사전점검")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.executable").type(JsonFieldType.BOOLEAN)
                                                .description("실행가능 여부"),
                                        fieldWithPath("data.malfunctionCause").type(JsonFieldType.STRING)
                                                .description("해당 챗플로우가 실행이 불가능한 이유")
                                )
                                .build())));
    }

}
