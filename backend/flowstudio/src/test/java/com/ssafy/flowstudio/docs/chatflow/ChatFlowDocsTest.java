package com.ssafy.flowstudio.docs.chatflow;

import com.ssafy.flowstudio.api.controller.chatflow.ChatFlowController;
import com.ssafy.flowstudio.api.service.chatflow.ChatFlowService;
import com.ssafy.flowstudio.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class ChatFlowDocsTest extends RestDocsSupport {

    private ChatFlowService chatFlowService = mock(ChatFlowService.class);

    @Override
    protected Object initController() {
        return new ChatFlowController(chatFlowService);
    }

    @DisplayName("챗플로우 목록을 조회한다")
    @Test
    void getChatFlows() {
        // given

        // when

        // then
    }

    @DisplayName("챗플로우를 조회한다")
    @Test
    void getChatFlow() {
        // given

        // when

        // then
    }

    @DisplayName("챗플로우를 생성한다")
    @Test
    void createChatFlow() {
        // given

        // when

        // then
    }

    @DisplayName("챗플로우를 수정한다")
    @Test
    void updateChatFlow() {
        // given

        // when

        // then
    }

    @DisplayName("챗플로우를 삭제한다")
    @Test
    void deleteChatFlow() {
        // given

        // when

        // then
    }

}
