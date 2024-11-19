package com.ssafy.flowstudio.domain.chatflow.repository;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.user.entity.User;

import java.util.List;

public interface CustomChatFlowRepository {
    List<ChatFlow> findByUser(User user);
}
