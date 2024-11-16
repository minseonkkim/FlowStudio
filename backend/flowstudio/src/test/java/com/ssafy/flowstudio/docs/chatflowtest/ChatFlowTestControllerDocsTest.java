package com.ssafy.flowstudio.docs.chatflowtest;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ssafy.flowstudio.api.controller.chatflowtest.ChatFlowTestController;
import com.ssafy.flowstudio.api.controller.chatflowtest.request.ChatFlowTestRequest;
import com.ssafy.flowstudio.api.service.chatflowtest.ChatFlowTestCreateResponse;
import com.ssafy.flowstudio.api.service.chatflowtest.ChatFlowTestService;
import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestCaseResponse;
import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestDetailResponse;
import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestListResponse;
import com.ssafy.flowstudio.docs.RestDocsSupport;
import com.ssafy.flowstudio.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatFlowTestControllerDocsTest extends RestDocsSupport {

    private final ChatFlowTestService chatFlowTestService = mock(ChatFlowTestService.class);

    @Override
    protected Object initController() {
        return new ChatFlowTestController(chatFlowTestService);
    }

    @DisplayName("챗플로우 테스트 목록 조회")
    @Test
    void getChats() throws Exception {
        // given
        ChatFlowTestListResponse response11 = ChatFlowTestListResponse.builder()
                .embeddingDistanceMean(1.0f)
                .embeddingDistanceVariance(0.1f)
                .crossEncoderMean(1.0f)
                .crossEncoderVariance(0.1f)
                .rougeMetricMean(1.0f)
                .rougeMetricVariance(0.1f)
                .totalTestCount(3)
                .build();

        ChatFlowTestListResponse response2 = ChatFlowTestListResponse.builder()
                .embeddingDistanceMean(1.0f)
                .embeddingDistanceVariance(0.1f)
                .crossEncoderMean(1.0f)
                .crossEncoderVariance(0.1f)
                .rougeMetricMean(1.0f)
                .rougeMetricVariance(0.1f)
                .totalTestCount(3)
                .build();

        given(chatFlowTestService.getChatFlowTests(any(User.class), any(Long.class)))
                .willReturn(List.of(response11, response2));

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/{chatFlowId}/tests", 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-chat-flow-tests",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlowTest")
                                .summary("챗플로우 테스트 목록 조회")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data[].embeddingDistanceMean").type(JsonFieldType.NUMBER)
                                                .description("평균"),
                                        fieldWithPath("data[].embeddingDistanceVariance").type(JsonFieldType.NUMBER)
                                                .description("평균"),
                                        fieldWithPath("data[].crossEncoderMean").type(JsonFieldType.NUMBER)
                                                .description("평균"),
                                        fieldWithPath("data[].crossEncoderVariance").type(JsonFieldType.NUMBER)
                                                .description("분산"),
                                        fieldWithPath("data[].rougeMetricMean").type(JsonFieldType.NUMBER)
                                                .description("평균"),
                                        fieldWithPath("data[].rougeMetricVariance").type(JsonFieldType.NUMBER)
                                                .description("분산"),
                                        fieldWithPath("data[].totalTestCount").type(JsonFieldType.NUMBER)
                                                .description("총 테스트 수")
                                )
                                .build())));
    }



    @DisplayName("챗플로우 테스트 상세 조회")
    @WithMockUser
    @Test
    void getChat() throws Exception {
        // given
        ChatFlowTestCaseResponse response1 = ChatFlowTestCaseResponse.builder()
                .id(1L)
                .groundTruth("groundTruth")
                .testQuestion("testQuestion")
                .prediction("prediction")
                .embeddingDistance(1.0f)
                .crossEncoder(1.0f)
                .rougeMetric(1.0f)
                .build();

        ChatFlowTestCaseResponse response2 = ChatFlowTestCaseResponse.builder()
                .id(2L)
                .groundTruth("groundTruth")
                .testQuestion("testQuestion")
                .prediction("prediction")
                .embeddingDistance(1.0f)
                .crossEncoder(1.0f)
                .rougeMetric(1.0f)
                .build();

        ChatFlowTestDetailResponse response = ChatFlowTestDetailResponse.builder()
                .id(1L)
                .embeddingDistanceMean(1.0f)
                .embeddingDistanceVariance(0.1f)
                .crossEncoderMean(1.0f)
                .crossEncoderVariance(0.1f)
                .rougeMetricMean(1.0f)
                .rougeMetricVariance(0.1f)
                .chatFlowTestCases(List.of(response1, response2))
                .build();

        given(chatFlowTestService.getChatFlowTest(any(User.class), any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/{chatFlowId}/tests/{chatFlowTestId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-chat-flow-test",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlowTest")
                                .summary("챗플로우 테스트 상세 조회")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 테스트 아이디"),
                                        fieldWithPath("data.embeddingDistanceMean").type(JsonFieldType.NUMBER)
                                                .description("평균"),
                                        fieldWithPath("data.embeddingDistanceVariance").type(JsonFieldType.NUMBER)
                                                .description("평균"),
                                        fieldWithPath("data.crossEncoderMean").type(JsonFieldType.NUMBER)
                                                .description("평균"),
                                        fieldWithPath("data.crossEncoderVariance").type(JsonFieldType.NUMBER)
                                                .description("분산"),
                                        fieldWithPath("data.rougeMetricMean").type(JsonFieldType.NUMBER)
                                                .description("평균"),
                                        fieldWithPath("data.rougeMetricVariance").type(JsonFieldType.NUMBER)
                                                .description("분산"),
                                        fieldWithPath("data.chatFlowTestCases[].id").type(JsonFieldType.NUMBER)
                                                .description("테스트케이스 아이디"),
                                        fieldWithPath("data.chatFlowTestCases[].testQuestion").type(JsonFieldType.STRING)
                                                .description("질문"),
                                        fieldWithPath("data.chatFlowTestCases[].groundTruth").type(JsonFieldType.STRING)
                                                .description("정답"),
                                        fieldWithPath("data.chatFlowTestCases[].prediction").type(JsonFieldType.STRING)
                                                .description("예측"),
                                        fieldWithPath("data.chatFlowTestCases[].embeddingDistance").type(JsonFieldType.NUMBER)
                                                .description("embeddingDistance"),
                                        fieldWithPath("data.chatFlowTestCases[].crossEncoder").type(JsonFieldType.NUMBER)
                                                .description("crossEncoder"),
                                        fieldWithPath("data.chatFlowTestCases[].rougeMetric").type(JsonFieldType.NUMBER)
                                                .description("rougeMetric")

                                )
                                .build())));
    }

    @DisplayName("챗플로우 테스트 생성")
    @Test
    void createChat() throws Exception {
        // given
        ChatFlowTestRequest request1 = ChatFlowTestRequest.builder()
                .groundTruth("groundTruth")
                .testQuestion("testQuestion")
                .build();

        ChatFlowTestRequest request2 = ChatFlowTestRequest.builder()
                .groundTruth("groundTruth")
                .testQuestion("testQuestion")
                .build();


        ChatFlowTestCreateResponse response1 = ChatFlowTestCreateResponse.builder()
                .chatId(1L)
                .testQuestion("testQuestion")
                .groundTruth("groundTruth")
                .build();

        ChatFlowTestCreateResponse response2 = ChatFlowTestCreateResponse.builder()
                .chatId(2L)
                .testQuestion("testQuestion")
                .groundTruth("groundTruth")
                .build();


        given(chatFlowTestService.createChatFlowTest(any(User.class), any(Long.class), any()))
                .willReturn(List.of(response1, response2));

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/{chatFlowId}/tests", 1L)
                        .content(objectMapper.writeValueAsString(List.of(request1, request2)))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("create-chat-flow-test",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlowTest")
                                .summary("챗플로우 테스트 생성")
                                .requestFields(
                                        fieldWithPath("[].groundTruth").type(JsonFieldType.STRING)
                                                .description("도착 노드 아이디"),
                                        fieldWithPath("[].testQuestion").type(JsonFieldType.STRING)
                                                .description("시작 조건 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data[].chatId").type(JsonFieldType.NUMBER)
                                                .description("챗 아이디"),
                                        fieldWithPath("data[].testQuestion").type(JsonFieldType.STRING)
                                                .description("testQuestion"),
                                        fieldWithPath("data[].groundTruth").type(JsonFieldType.STRING)
                                                .description("groundTruth")
                                )
                                .build())));
    }

}
