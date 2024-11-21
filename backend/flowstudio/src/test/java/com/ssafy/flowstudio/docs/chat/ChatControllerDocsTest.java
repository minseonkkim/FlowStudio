package com.ssafy.flowstudio.docs.chat;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ssafy.flowstudio.api.controller.chat.ChatController;
import com.ssafy.flowstudio.api.controller.chat.request.ChatCreateRequest;
import com.ssafy.flowstudio.api.controller.chat.request.ChatMessageRequest;
import com.ssafy.flowstudio.api.service.chat.ChatService;
import com.ssafy.flowstudio.api.service.chat.response.ChatCreateResponse;
import com.ssafy.flowstudio.api.service.chat.response.ChatDetailResponse;
import com.ssafy.flowstudio.api.service.chat.response.ChatListResponse;
import com.ssafy.flowstudio.api.service.chat.response.ChatSimpleResponse;
import com.ssafy.flowstudio.docs.RestDocsSupport;
import com.ssafy.flowstudio.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatControllerDocsTest extends RestDocsSupport {

    private final ChatService chatService = mock(ChatService.class);

    @Override
    protected Object initController() {
        return new ChatController(chatService);
    }


    @DisplayName("채팅 목록을 조회한다.")
    @Test
    void getChatFlows() throws Exception {
        // given
        ChatSimpleResponse chat1 = ChatSimpleResponse.builder()
                .id(1L)
                .title("title1")
                .build();

        ChatSimpleResponse chat2 = ChatSimpleResponse.builder()
                .id(2L)
                .title("title2")
                .build();

        ChatListResponse response = ChatListResponse.builder()
                .id(1L)
                .title("title")
                .thumbnail("1")
                .chats(List.of(chat1, chat2))
                .build();

        given(chatService.getChats(any(User.class), any(Long.class), anyInt(), anyInt()))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/{chatFlowId}/chats", 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-chats",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Chat")
                                .summary("채팅 목록")
                                .queryParameters(
                                        RequestDocumentation.parameterWithName("page").optional().description("조회할 페이지, 입력 없으면 default 0"),
                                        RequestDocumentation.parameterWithName("size").optional().description("사이즈, 입력 없으면 default 20")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                                .description("챗플로우 아이디"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING)
                                                .description("챗플로우 제목"),
                                        fieldWithPath("data.thumbnail").type(JsonFieldType.STRING)
                                                .description("챗플로우 썸네일"),
                                        fieldWithPath("data.chats").type(JsonFieldType.ARRAY)
                                                .description("채팅 목록"),
                                        fieldWithPath("data.chats[].id").type(JsonFieldType.NUMBER)
                                                .description("채팅 아이디"),
                                        fieldWithPath("data.chats[].title").type(JsonFieldType.STRING)
                                                .description("채팅 제목"))
                                .build())));
    }

    @DisplayName("채팅 상세 조회")
    @Test
    void getChat() throws Exception {
        // given
        ChatDetailResponse response = ChatDetailResponse.builder()
                .id(1L)
                .title("title")
                .messageList("[{question: 'question', answer: 'answer'}]")
                .build();

        given(chatService.getChat(any(User.class), any(Long.class), any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/{chatFlowId}/chats/{chatId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-chat",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Chat")
                                .summary("채팅 상세 조회")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                                .description("채팅 아이디"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING)
                                                .description("채팅 제목"),
                                        fieldWithPath("data.messageList").type(JsonFieldType.STRING)
                                                .description("대화 내용")
                                )
                                .build())));
    }

    @DisplayName("채팅을 생성한다.")
    @Test
    void createChat() throws Exception {
        // given
        ChatCreateRequest request = ChatCreateRequest.builder()
                .isPreview(true)
                .build();

        ChatCreateResponse response = ChatCreateResponse.builder()
                .id(1L)
                .title("title")
                .isPreview(true)
                .build();

        given(chatService.createChat(any(User.class), anyLong(), any(), any()))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/{chatFlowId}/chats", 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("create-chat",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Chat")
                                .summary("채팅 생성")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                                .description("채팅 아이디"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING)
                                                .description("채팅 제목"),
                                        fieldWithPath("data.preview").type(JsonFieldType.BOOLEAN)
                                                .description("미리보기 여부")
                                )
                                .build())));

    }

    @DisplayName("채팅을 보낸다.")
    @Test
    void sendChatMessage() throws Exception {
        // given
        ChatMessageRequest request = ChatMessageRequest.builder()
                .message("message")
                .build();

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chats/{chatId}", 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("send-message",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Chat")
                                .summary("메시지 보내기")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data").type(JsonFieldType.NULL)
                                                .description("데이터"))
                                .build())));

    }

    @DisplayName("채팅 삭제")
    @Test
    void deleteChat() throws Exception {
        given(chatService.deleteChat(any(User.class), any(Long.class)))
                .willReturn(true);

        // when
        ResultActions perform = mockMvc.perform(
                delete("/api/v1/chat-flows/{chatFlowId}/chats/{chatId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-chat",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Chat")
                                .summary("채팅 삭제")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data").type(JsonFieldType.NULL)
                                                .description("데이터"))
                                .build())));
    }

}
