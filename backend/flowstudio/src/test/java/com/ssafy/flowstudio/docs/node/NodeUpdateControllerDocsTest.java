package com.ssafy.flowstudio.docs.node;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ssafy.flowstudio.api.controller.node.NodeUpdateController;
import com.ssafy.flowstudio.api.controller.node.request.CoordinateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.AnswerUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.LlmUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.QuestionClassifierUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.RetrieverUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.StartUpdateRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.node.NodeUpdateService;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NodeUpdateControllerDocsTest extends RestDocsSupport {

    private final NodeUpdateService nodeUpdateService = mock(NodeUpdateService.class);

    @Override
    protected Object initController() {
        return new NodeUpdateController(nodeUpdateService);
    }

    @DisplayName("시작 노드를 수정한다")
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
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-start",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Node")
                                .summary("시작 노드 수정")
                                .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING)
                                                .description("변경할 이름"),
                                        fieldWithPath("coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("변경할 x좌표"),
                                        fieldWithPath("coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("변경할 y좌표"),
                                        fieldWithPath("maxLength").type(JsonFieldType.NUMBER)
                                                .description("변경할 입력 최대 길이")
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
                                        fieldWithPath("data.outputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("outputEdges"),
                                        fieldWithPath("data.inputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("inputEdges"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                                                .description("변경할 이름"),
                                        fieldWithPath("data.coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("변경할 x좌표"),
                                        fieldWithPath("data.coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("변경할 y좌표"),
                                        fieldWithPath("data.maxLength").type(JsonFieldType.NUMBER)
                                                .description("변경할 입력 최대 길이")
                                )
                                .build())));

    }

    @DisplayName("질문분류기 노드를 수정한다")
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
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-question-classifier",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Node")
                                .summary("질문 분류기 노드 수정")
                                .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING)
                                                .description("변경할 이름"),
                                        fieldWithPath("coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("변경할 x좌표"),
                                        fieldWithPath("coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("변경할 y좌표")
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
                                        fieldWithPath("data.outputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("outputEdges"),
                                        fieldWithPath("data.inputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("inputEdges"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                                                .description("변경할 이름"),
                                        fieldWithPath("data.coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("변경할 x좌표"),
                                        fieldWithPath("data.coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("변경할 y좌표"),
                                        fieldWithPath("data.questionClasses").type(JsonFieldType.ARRAY).optional()
                                                .description("질문 클래스 조건들")
                                )
                                .build())));

    }

    @DisplayName("Llm 노드를 수정한다")
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
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-llm",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Node")
                                .summary("LLM 노드 수정")
                                .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING)
                                                .description("변경할 이름"),
                                        fieldWithPath("coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("변경할 x좌표"),
                                        fieldWithPath("coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("변경할 y좌표"),
                                        fieldWithPath("promptSystem").type(JsonFieldType.STRING)
                                                .description("promptSystem"),
                                        fieldWithPath("promptUser").type(JsonFieldType.STRING)
                                                .description("promptUser"),
                                        fieldWithPath("context").type(JsonFieldType.STRING)
                                                .description("context"),
                                        fieldWithPath("temperature").type(JsonFieldType.NUMBER)
                                                .description("temperature"),
                                        fieldWithPath("maxTokens").type(JsonFieldType.NUMBER)
                                                .description("maxTokens")
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
                                        fieldWithPath("data.outputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("outputEdges"),
                                        fieldWithPath("data.inputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("inputEdges"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                                                .description("변경할 이름"),
                                        fieldWithPath("data.coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("변경할 x좌표"),
                                        fieldWithPath("data.coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("변경할 y좌표"),
                                        fieldWithPath("data.promptSystem").type(JsonFieldType.STRING)
                                                .description("promptSystem"),
                                        fieldWithPath("data.promptUser").type(JsonFieldType.STRING)
                                                .description("promptUser"),
                                        fieldWithPath("data.precedingNodes").type(JsonFieldType.ARRAY)
                                                .optional()
                                                .description("이전 노드들의 ID 목록 (없을 경우 null)"),
                                        fieldWithPath("data.context").type(JsonFieldType.STRING)
                                                .description("현재 대화의 컨텍스트 정보"),
                                        fieldWithPath("data.temperature").type(JsonFieldType.NUMBER)
                                                .description("응답 생성 시의 다양성 설정 값 (예: 0.5는 중간 정도의 다양성)"),
                                        fieldWithPath("data.maxTokens").type(JsonFieldType.NUMBER)
                                                .description("생성될 텍스트의 최대 토큰 수"),
                                        fieldWithPath("data.modelProvider").type(JsonFieldType.STRING).optional()
                                                .description("사용할 언어 모델의 제공자 (예: OpenAI)"),
                                        fieldWithPath("data.modelName").type(JsonFieldType.STRING).optional()
                                                .description("사용할 언어 모델의 이름 (예: GPT-3)"),
                                        fieldWithPath("data.coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("변경할 x좌표"),
                                        fieldWithPath("data.coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("변경할 y좌표"),
                                        fieldWithPath("data.promptSystem").type(JsonFieldType.STRING)
                                                .description("시스템 프롬프트"),
                                        fieldWithPath("data.promptUser").type(JsonFieldType.STRING)
                                                .description("사용자 프롬프트")
                                )
                                .build())));
    }


    @DisplayName("Retriever 노드를 수정한다")
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
                .intervalTime(10)
                .query("query")
                .scoreThreshold(0.5f)
                .topK(10)
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
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-retriever",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Node")
                                .summary("지식 검색기 노드 수정")
                                .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING)
                                                .description("변경할 이름"),
                                        fieldWithPath("coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("변경할 x좌표"),
                                        fieldWithPath("coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("변경할 y좌표"),
                                        fieldWithPath("knowledgeId").type(JsonFieldType.NUMBER)
                                                .description("지식 베이스의 ID"),
                                        fieldWithPath("intervalTime").type(JsonFieldType.NUMBER)
                                                .description("요청 간의 시간 간격 (초 단위)"),
                                        fieldWithPath("topK").type(JsonFieldType.NUMBER)
                                                .description("검색 결과 상위 K개 항목"),
                                        fieldWithPath("scoreThreshold").type(JsonFieldType.NUMBER)
                                                .description("결과의 점수 임계값 (예: 0.5 이상)"),
                                        fieldWithPath("query").type(JsonFieldType.STRING)
                                                .description("검색 쿼리 문자열")
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
                                        fieldWithPath("data.outputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("outputEdges"),
                                        fieldWithPath("data.inputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("inputEdges"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                                                .description("변경할 이름"),
                                        fieldWithPath("data.coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("변경할 x좌표"),
                                        fieldWithPath("data.coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("변경할 y좌표"),
                                        fieldWithPath("data.knowledgeId").type(JsonFieldType.NUMBER)
                                                .description("지식 베이스의 ID"),
                                        fieldWithPath("data.intervalTime").type(JsonFieldType.NUMBER)
                                                .description("요청 간의 시간 간격 (초 단위)"),
                                        fieldWithPath("data.topK").type(JsonFieldType.NUMBER)
                                                .description("검색 결과 상위 K개 항목"),
                                        fieldWithPath("data.scoreThreshold").type(JsonFieldType.NUMBER)
                                                .description("결과의 점수 임계값 (예: 0.5 이상)"),
                                        fieldWithPath("data.query").type(JsonFieldType.STRING)
                                                .description("검색 쿼리 문자열"),
                                        fieldWithPath("data.knowledge").type(JsonFieldType.STRING).optional()
                                                .description("지식")
                                )
                                .build())));
    }

    @DisplayName("Answer 노드를 수정한다")
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
                .outputMessage("outputMessage")
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
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-answer",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Node")
                                .summary("답변 노드 수정")
                                .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING)
                                                .description("변경할 이름"),
                                        fieldWithPath("coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("변경할 x좌표"),
                                        fieldWithPath("coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("변경할 y좌표"),
                                        fieldWithPath("outputMessage").type(JsonFieldType.STRING)
                                                .description("출력 메시지")
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
                                        fieldWithPath("data.outputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("outputEdges"),
                                        fieldWithPath("data.inputEdges").optional().type(JsonFieldType.ARRAY)
                                                .description("inputEdges"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                                                .description("변경할 이름"),
                                        fieldWithPath("data.coordinate.x").type(JsonFieldType.NUMBER)
                                                .description("변경할 x좌표"),
                                        fieldWithPath("data.coordinate.y").type(JsonFieldType.NUMBER)
                                                .description("변경할 y좌표"),
                                        fieldWithPath("data.outputMessage").type(JsonFieldType.STRING)
                                                .description("출력 메시지")
                                )
                                .build())));
    }

}
