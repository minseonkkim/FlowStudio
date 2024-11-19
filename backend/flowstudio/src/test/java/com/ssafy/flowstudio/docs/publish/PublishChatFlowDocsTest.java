package com.ssafy.flowstudio.docs.publish;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ssafy.flowstudio.api.controller.chatflow.ChatFlowController;
import com.ssafy.flowstudio.api.controller.chatflow.request.ChatFlowRequest;
import com.ssafy.flowstudio.api.service.chatflow.ChatFlowService;
import com.ssafy.flowstudio.api.service.chatflow.request.ChatFlowServiceRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.*;
import com.ssafy.flowstudio.api.service.node.response.*;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeResponse;
import com.ssafy.flowstudio.api.service.user.response.UserResponse;
import com.ssafy.flowstudio.docs.RestDocsSupport;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.publish.PublishChatFlowController;
import com.ssafy.flowstudio.publish.PublishService;
import com.ssafy.flowstudio.publish.response.PublishChatFlowResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PublishChatFlowDocsTest extends RestDocsSupport {

    private PublishService publishService = mock(PublishService.class);

    @Override
    protected Object initController() {
        return new PublishChatFlowController(publishService);
    }

    @DisplayName("내 발행한 챗플로우 목록을 조회한다")
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

        given(publishService.getPublishChatFlows(any(User.class)))
                .willReturn(List.of(response));

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/publish")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-publish-chatflow-list",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("발행한 챗플로우 목록 조회")
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


    @DisplayName("챗플로우를 발행한다")
    @Test
    void publishChatFlow() throws Exception {
        PublishChatFlowResponse publishChatFlowResponse = PublishChatFlowResponse.builder()
                .chatFlowId(1L)
                .publishUrl("UUID")
                .publishedAt(LocalDateTime.now())
                .build();

        given(publishService.publishChatFlow(any(User.class), any(Long.class)))
                .willReturn(publishChatFlowResponse);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/{chatFlowId}/publish", 1L)
                        .contentType(MediaType.APPLICATION_JSON));
        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("publish-chatflow",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("챗플로우 발행")
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
                                        fieldWithPath("data.chatFlowId").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 ID"),
                                        fieldWithPath("data.publishUrl").type(JsonFieldType.STRING)
                                                .description("챗플로우 URL"),
                                        fieldWithPath("data.publishedAt").type(JsonFieldType.STRING)
                                                .description("챗플로우 발행 날짜")
                                )
                                .build())));
    }

    @DisplayName("챗플로우 발행을 취소한다")
    @Test
    void unPublishChatFlow() throws Exception {
        given(publishService.unPublishChatFlow(any(User.class), any(Long.class)))
                .willReturn(Boolean.TRUE);

        // when
        ResultActions perform = mockMvc.perform(
                delete("/api/v1/chat-flows/{chatFlowId}/publish", 1L)
                        .contentType(MediaType.APPLICATION_JSON));
        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("unpublish-chatflow",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatFlow")
                                .summary("챗플로우 발행 취소")
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
                                        fieldWithPath("data").type(JsonFieldType.BOOLEAN)
                                                .description("data")
                                )
                                .build())));
    }

}
