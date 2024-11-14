package com.ssafy.flowstudio.docs.chatflowtest;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ssafy.flowstudio.api.controller.chatflowtest.ChatFlowTestController;
import com.ssafy.flowstudio.api.controller.chatflowtest.request.ChatFlowTestRequest;
import com.ssafy.flowstudio.api.service.chatflowtest.ChatFlowTestService;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatFlowTestControllerDocsTest extends RestDocsSupport {

    private final ChatFlowTestService chatFlowTestService = mock(ChatFlowTestService.class);

    @Override
    protected Object initController() {
        return new ChatFlowTestController(chatFlowTestService);
    }

    @DisplayName("챗플로우 테스트 생성")
    @WithMockUser
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

        given(chatFlowTestService.createChatFlowTest(any(User.class), any(Long.class), any()))
                .willReturn(List.of(1L, 2L));

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/{chatFlowId}/tests", 1L)
                        .with(csrf())
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
                                        fieldWithPath("data").type(JsonFieldType.ARRAY)
                                                .description("챗 아이디")
                                )
                                .build())));
    }
}
