package com.ssafy.flowstudio.api.service.chatflow;

import com.ssafy.flowstudio.api.controller.chatflow.request.ChatFlowCreateRequest;
import com.ssafy.flowstudio.api.service.chatflow.request.ChatFlowCreateServiceRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowListResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowUpdateResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ChatFlowService {

    public List<ChatFlowListResponse> getChatFlows(User user) {
        return null;
    }

    public ChatFlowResponse getChatFlow(User user, Long chatFlowId) {
        return null;
    }

    @Transactional
    public ChatFlowResponse createChatFlow(User user, ChatFlowCreateRequest request) {
        return null;
    }

    @Transactional
    public boolean deleteChatFlow(User user, Long chatFlowId) {
        return true;
    }

    @Transactional
    public ChatFlowUpdateResponse updateChatFlow(User user, Long chatFlowId, ChatFlowCreateServiceRequest request) {
        return null;
    }

}
